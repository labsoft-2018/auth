(ns auth.logic.token
  (:require [schema.core :as s]
            [auth.wire.token :as wire.token]
            [auth.models.user :as models.user]
            [common-labsoft.protocols.token :as protocols.token]))

(s/defn user->token :- wire.token/Token
  [{:keys [user/id user/type]} :- models.user/User]
  {:token/sub    (str id)
   :token/type   (-> type name keyword)
   :token/scopes #{(name type)}})

(s/defn service->token :- wire.token/Token
  [service-name :- s/Str, scopes :- #{s/Str}]
  {:token/sub    service-name
   :token/type   :service
   :token/scopes scopes})

(s/defn bearer-token :- s/Str
  [token :- wire.token/Token, token-encode :- protocols.token/IToken]
  (->> (protocols.token/encode token-encode token)
       (str "Bearer ")))
