(ns auth.adapters.facebook-test
  (:require [midje.sweet :refer :all]
            [auth.adapters.facebook :as adapters.facebook]))

(facts "when internalizing facebook responses"
  (fact "on `fb-user->register`"
    (adapters.facebook/fb-user->register {:fb-user/id    "fb-id"
                                          :fb-user/email "email"}) => {:register/email     "email"
                                                                       :register/cred-type :credential.type/facebook
                                                                       :register/type      :user.type/customer
                                                                       :register/fb-id     "fb-id"}))
