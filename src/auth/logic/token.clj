(ns auth.logic.token
  (:require [schema.core :as s]
            [auth.models.token :as models.token]
            [auth.models.user :as models.user]
            [common-labsoft.protocols.token :as protocols.token]))

(s/defn user->token :- models.token/Token
  [{:keys [user/id user/type]} :- models.user/User]
  {:token/user-id   id
   :token/user-type type
   :token/scopes    #{(name type)}})

(s/defn bearer-token :- s/Str
  [token :- models.token/Token, token-encode :- protocols.token/IToken]
  (->> (protocols.token/encode token-encode token)
       (str "Bearer ")))
