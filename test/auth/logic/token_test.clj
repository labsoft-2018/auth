(ns auth.logic.token-test
  (:require [midje.sweet :refer :all]
            [auth.logic.token :as logic.token]
            [common-labsoft.misc :as misc]
            [common-labsoft.protocols.token :as protocols.token]))

(def user-id (misc/uuid))
(def created-at #new/time "2018-10-10T00:00:00Z")

(facts "when creating tokens"
  (fact "on `user->token`"
    (logic.token/user->token {:user/id         user-id
                              :user/email      "teste@teste.com.br"
                              :user/type       :user.type/customer
                              :user/created-at created-at}) => {:token/type   :customer
                                                                :token/sub    (str user-id)
                                                                :token/scopes #{"customer"}})
  (fact "on `service->token`"
    (logic.token/service->token "test-service" #{"scope1"}) => {:token/type   :service
                                                                :token/sub    "test-service"
                                                                :token/scopes #{"scope1"}})

  (fact "on `bearer-token`"
    (logic.token/bearer-token ..token.. ..token-encoder..) => "Bearer base64token"
    (provided
      (protocols.token/encode ..token-encoder.. ..token..) => "base64token")))
