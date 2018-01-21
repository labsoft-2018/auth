(ns auth.logic.credential-test
  (:require [midje.sweet :refer :all]
            [auth.logic.credential :as logic.credential]
            [common-labsoft.misc :as misc]
            [common-labsoft.protocols.crypto :as protocols.crypto]))

(def user-id (misc/uuid))
(def pass-creds {:credential/type :credential.type/password
                 :credential/email "email@test.com"
                 :credential/encrypted-password "bcryptedpass"
                 :credential/user-id user-id})

(facts "when we create new credentials"
  (fact "on `base-credential`"
    (logic.credential/base-credential user-id "email@test.com" :credential.type/password)
    => {:credential/type :credential.type/password
        :credential/email "email@test.com"
        :credential/user-id user-id}))

(facts "utilities functions for credentials"
  (fact "on `valid-cred-type?`"
    (logic.credential/valid-cred-type? :user.type/customer :credential.type/password) => truthy
    (logic.credential/valid-cred-type? :user.type/customer :credential.type/facebook) => truthy
    (logic.credential/valid-cred-type? :user.type/merchant :credential.type/password) => truthy
    (logic.credential/valid-cred-type? :user.type/merchant :credential.type/facebook) => falsey
    (logic.credential/valid-cred-type? :user.type/carrier :credential.type/password) => truthy
    (logic.credential/valid-cred-type? :user.type/carrier :credential.type/facebook) => falsey))

(facts "when checking for credentials"
  (fact "on `check-pass-credential` - success"
    (logic.credential/check-pass-credential pass-creds "123" ..crypto..) => pass-creds
    (provided
      (protocols.crypto/check ..crypto.. "bcryptedpass" "123") => true))

  (fact "on `check-pass-credential` - failure"
    (logic.credential/check-pass-credential pass-creds "123" ..crypto..) => nil
    (provided
      (protocols.crypto/check ..crypto.. "bcryptedpass" "123") => false)))
