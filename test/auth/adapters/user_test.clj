(ns auth.adapters.user-test
  (:require [midje.sweet :refer :all]
            [auth.adapters.user :as adapters.user]
            [common-labsoft.misc :as misc]
            [common-labsoft.protocols.token :as protocols.token]))

(def user-id (misc/uuid))

(facts "when externalizing an user"
  (fact "on `authenticated-user->user-with-jwt`"
    (adapters.user/authenticated-user->user-with-jwt {:user/email "test"
                                                      :user/type  :user.type/customer
                                                      :user/id    user-id
                                                      :user/token ..token..} ..token-encoder..)
    => {:user/email "test"
        :user/type  :user.type/customer
        :user/id    user-id
        :user/token {:token/jwt "Bearer 123"}}
    (provided
      (protocols.token/encode ..token-encoder.. ..token..) => "123")))
