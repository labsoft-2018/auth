(ns auth.adapters.user
  (:require [schema.core :as s]
            [auth.wire.user :as wire.user]
            [auth.adapters.token :as adapters.token]
            [common-labsoft.protocols.token :as protocols.token]))

(s/defn authenticated-user->user-with-jwt :- wire.user/UserWithJwtToken
  [user :- wire.user/AuthenticatedUser, token :- protocols.token/Token]
  (update user :user/token #(adapters.token/token->jwt-token % token)))
