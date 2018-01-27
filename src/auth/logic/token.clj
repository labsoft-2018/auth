(ns auth.logic.token
  (:require [schema.core :as s]
            [auth.wire.token :as wire.token]
            [auth.models.user :as models.user]))

(s/defn user->token :- wire.token/Token
  [{:keys [user/id user/type user/email]} :- models.user/User]
  {:token/sub    (str id)
   :token/type   (-> type name keyword)
   :token/scopes #{(name type)}
   :token/email  email})

(s/defn service->token :- wire.token/Token
  [service-name :- s/Str, scopes :- #{s/Str}]
  {:token/sub    service-name
   :token/type   :service
   :token/scopes scopes
   :token/email  (str service-name "@labsoft.host")})
