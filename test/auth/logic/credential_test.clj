(ns auth.logic.credential-test
  (:require [midje.sweet :refer :all]
            [auth.logic.credential :as logic.credential]
            [common-labsoft.misc :as misc]))

(def user-id (misc/uuid))

(facts "when we create new credentials"
  (fact "on `base-credential`"
    (logic.credential/base-credential user-id "email@test.com" :credential.type/password)
    => {:credential/type :credential.type/password
        :credential/email "email@test.com"
        :credential/user-id user-id}))
