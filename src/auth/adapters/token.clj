(ns auth.adapters.token
  (:require [schema.core :as s]
            [auth.wire.token :as wire.token]
            [common-labsoft.protocols.token :as protocols.token]))

(s/defn token->base64 :- s/Str
  [token :- wire.token/Token, token-service :- protocols.token/IToken]
  (str "Bearer " (protocols.token/encode token-service token)))

(s/defn token->jwt-token :- wire.token/JwtBearerToken
  [token :- wire.token/Token, token-service :- protocols.token/IToken]
  {:token/jwt (token->base64 token token-service)})
