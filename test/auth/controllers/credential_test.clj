(ns auth.controllers.credential-test
  (:require [midje.sweet :refer :all]
            [auth.controllers.credential :as controllers.credential]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [auth.logic.credential :as logic.credential]
            [auth.db.datomic.credential :as datomic.credential]))

(def pass-credential {:credential/type :credential.type/password})

(facts "when creating a new credential"
  (let [register {:register/email     ..email..
                  :register/password  ..pass..
                  :register/cred-type ..cred-type..}]
    (fact "on `prepare-credential!` - using password"
      (controllers.credential/prepare-credential! pass-credential register ..crypto..)
      => {:credential/type               :credential.type/password
          :credential/encrypted-password ..enc-pass..}
      (provided
        (protocols.crypto/bcrypt ..crypto.. ..pass..) => ..enc-pass..))

    (fact "on `new-credential!`"
      (controllers.credential/new-credential! {:user/id ..user-id..} register ..crypto.. ..datomic..) => ..credential..
      (provided
        (logic.credential/base-credential ..user-id.. ..email.. ..cred-type..) => ..base-cred..
        (controllers.credential/prepare-credential! ..base-cred.. register ..crypto..) => ..secure-cred..
        (datomic.credential/new-credential! ..secure-cred.. ..datomic..) => ..credential..))))

(facts "whe authenticating new users request for token"
  (fact "using password - success"
    (controllers.credential/authenticate-request! {:auth/email     ..email..
                                                   :auth/password  ..password..
                                                   :auth/cred-type :credential.type/password} ..datomic.. ..crypto..)
    => ..credential..
    (provided
      (datomic.credential/email->credential ..email.. ..datomic..) => ..credential..
      (logic.credential/check-pass-credential ..credential.. ..password.. ..crypto..) => ..credential..))

  (fact "using password - failure"
    (controllers.credential/authenticate-request! {:auth/email     ..email..
                                                   :auth/password  ..password..
                                                   :auth/cred-type :credential.type/password} ..datomic.. ..crypto..)
    => (throws Exception)
    (provided
      (datomic.credential/email->credential ..email.. ..datomic..) => ..credential..
      (logic.credential/check-pass-credential ..credential.. ..password.. ..crypto..) => nil)))
