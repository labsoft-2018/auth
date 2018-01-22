(ns auth.logic.user-test
  (:require [midje.sweet :refer :all]
            [auth.logic.user :as logic.user]
            [common-labsoft.misc :as misc]))

(def user-id (misc/uuid))

(facts "when we are creating an user from a registration"
  (fact "on `register->user`"
    (logic.user/register->user {:register/type  :user.type/customer
                                :register/email "email"}) => {:user/type  :user.type/customer
                                                              :user/email "email"}))

(facts "when authenticating an user"
  (fact "on `user->authenticated-user`"
    (logic.user/user->authenticated-user {:user/type  :user.type/customer
                                          :user/email "teste"
                                          :user/id    user-id}) => {:user/type  :user.type/customer
                                                                    :user/email "teste"
                                                                    :user/id    user-id
                                                                    :user/token {:token/scopes #{"customer"}
                                                                                 :token/type   :customer
                                                                                 :token/sub    (str user-id)}}))
