(ns auth.adapters.token-test
  (:require [midje.sweet :refer :all]
            [auth.adapters.token :as adapters.token]
            [common-labsoft.protocols.token :as protocols.token]))

(facts "when externalizing the token"
  (fact "on `token->base64`"
    (adapters.token/token->base64 ..token.. ..token-encoder..) => "Bearer base64token"
    (provided
      (protocols.token/encode ..token-encoder.. ..token..) => "base64token"))

  (fact "on `token->jwt-token`"
    (adapters.token/token->jwt-token ..token.. ..token-encoder..) => {:token/jwt "Bearer base64token"}
    (provided
      (protocols.token/encode ..token-encoder.. ..token..) => "base64token")))
