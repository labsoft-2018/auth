(ns auth.controllers.token
  (:require [schema.core :as s]
            [common-labsoft.protocols.config :as protocols.config]
            [common-labsoft.protocols.token :as protocols.token]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [common-labsoft.exception :as exception]
            [auth.wire.auth :as wire.auth]
            [auth.logic.token :as logic.token]))

(s/defn service-token! :- s/Str
  [{:keys [auth/service auth/password]} :- wire.auth/ServiceAuthRequest
   config :- protocols.config/IConfig
   token :- protocols.token/IToken
   crypto :- protocols.crypto/ICrypto]
  (let [service-config (protocols.config/get-in! config [:trusted-clients service])]
    (if (protocols.crypto/check crypto (:password service-config) password)
      (-> (logic.token/service->token service (:scopes service-config))
          (logic.token/bearer-token token))
      (exception/forbidden! {:service-token :credentials-do-not-match}))))
