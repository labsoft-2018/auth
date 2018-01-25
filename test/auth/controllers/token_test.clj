(ns auth.controllers.token-test
  (:require [midje.sweet :refer :all]
            [auth.controllers.token :as controllers.token]
            [common-labsoft.protocols.config :as protocols.config]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [auth.logic.token :as logic.token]
            [auth.controllers.credential :as controllers.credential]
            [auth.controllers.user :as controllers.user]))

(facts "when generating a new token for a service"
  (fact "on `service-token!` - the check was successfully made"
    (controllers.token/service-token! {:auth/service  ..service..
                                       :auth/password ..password..} ..config.. ..crypto..) => ..service-token..
    (provided
      (protocols.config/get-in! ..config.. [:trusted-clients ..service..]) => {:password ..enc-pass.. :scopes ..scopes..}
      (protocols.crypto/check ..crypto.. ..enc-pass.. ..password..) => true
      (logic.token/service->token ..service.. ..scopes..) => ..service-token..))

  (fact "on `service-token!` - the check has failed"
    (controllers.token/service-token! {:auth/service  ..service..
                                       :auth/password ..password..} ..config.. ..crypto..) => (throws Exception)
    (provided
      (protocols.config/get-in! ..config.. [:trusted-clients ..service..]) => {:password ..enc-pass.. :scopes ..scopes..}
      (protocols.crypto/check ..crypto.. ..enc-pass.. ..password..) => false)))

(facts "when generating a new token for an user"
  (facts "using a password credential"
    (against-background
      ..auth-request.. =contains=> {:auth/cred-type :credential.type/password
                                    :auth/user-type ..user-type..})
    (fact "on `user-token!` - success"
      (controllers.token/user-token! ..auth-request.. ..sqs.. ..datomic.. ..crypto.. ..http..) => ..authenticated-user..
      (provided
        (controllers.credential/authenticate-pass-request! ..auth-request.. ..datomic.. ..crypto..) => ..credential..
        (controllers.user/credential->authenticated-user ..credential.. ..datomic..) => ..authenticated-user..)))

  (facts "using facebook credentials"
    (against-background
      ..auth-request.. =contains=> {:auth/cred-type :credential.type/facebook
                                    :auth/user-type :user.type/customer})
    (fact "on `user->token!` - success"
      (controllers.token/user-token! ..auth-request.. ..sqs.. ..datomic.. ..crypto.. ..http..) => ..authenticated-user..
      (provided
        (controllers.credential/authenticate-fb-request! ..auth-request.. ..datomic.. ..http..) => ..credential..
        (controllers.user/credential->authenticated-user ..credential.. ..datomic..) => ..authenticated-user..))

    (fact "on `user->token!` - needs to register user"
      (controllers.token/user-token! ..auth-request.. ..sqs.. ..datomic.. ..crypto.. ..http..) => ..authenticated-user..
      (provided
        (controllers.credential/authenticate-fb-request! ..auth-request.. ..datomic.. ..http..) => nil
        (controllers.user/try-register-facebook-user! ..auth-request.. ..crypto.. ..sqs.. ..datomic.. ..http..) => ..authenticated-user..))))
