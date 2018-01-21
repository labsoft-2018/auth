(ns auth.logic.user-test
  (:require [midje.sweet :refer :all]
            [auth.logic.user :as logic.user]))

(facts "when we are creating an user from a registration"
  (fact "on `register->user`"
    (logic.user/register->user {:register/type  :user.type/customer
                                :register/email "email"}) => {:user/type  :user.type/customer
                                                              :user/email "email"}))
