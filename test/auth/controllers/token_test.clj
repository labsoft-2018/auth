(ns auth.controllers.token-test
  (:require [midje.sweet :refer :all]
            [auth.controllers.token :as controllers.token]
            [common-labsoft.protocols.config :as protocols.config]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [auth.logic.token :as logic.token]
            [auth.logic.credential :as logic.credential]
            [auth.controllers.credential :as controllers.credential]
            [auth.db.datomic.user :as datomic.user]
            [auth.logic.user :as logic.user]))

(facts "when generating a new token for a service"
  (fact "on `service-token!` - the check was successfully made"
    (controllers.token/service-token! {:auth/service ..service..
                                       :auth/password ..password..} ..config.. ..crypto..) => ..service-token..
    (provided
      (protocols.config/get-in! ..config.. [:trusted-clients ..service..]) => {:password ..enc-pass.. :scopes ..scopes..}
      (protocols.crypto/check ..crypto.. ..enc-pass.. ..password..) => true
      (logic.token/service->token ..service.. ..scopes..) => ..service-token..))

  (fact "on `service-token!` - the check has failed"
    (controllers.token/service-token! {:auth/service ..service..
                                       :auth/password ..password..} ..config.. ..crypto..) => (throws Exception)
    (provided
      (protocols.config/get-in! ..config.. [:trusted-clients ..service..]) => {:password ..enc-pass.. :scopes ..scopes..}
      (protocols.crypto/check ..crypto.. ..enc-pass.. ..password..) => false)))

(facts "when generating a new token for an user"
  (facts "using a password credential"
    (fact "on `user-token!` - success"
      (controllers.token/user-token! ..auth-request.. ..datomic.. ..crypto..) => ..authenticated-user..
      (provided
        ..auth-request.. =contains=> {:auth/cred-type ..cred-type.. :auth/user-type ..user-type..}
        (logic.credential/valid-cred-type? ..user-type.. ..cred-type..) => true
        (controllers.credential/authenticate-request! ..auth-request.. ..datomic.. ..crypto..) => ..credential..
        ..credential.. =contains=> {:credential/user-id ..user-id..}
        (datomic.user/lookup! ..user-id.. ..datomic..) => ..user..
        (logic.user/user->authenticated-user ..user..) => ..authenticated-user..))))
