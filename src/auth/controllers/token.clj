(ns auth.controllers.token
  (:require [schema.core :as s]
            [common-labsoft.protocols.config :as protocols.config]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [common-labsoft.exception :as exception]
            [auth.wire.auth :as wire.auth]
            [auth.logic.token :as logic.token]
            [auth.logic.credential :as logic.credential]
            [auth.controllers.credential :as controllers.credential]
            [auth.db.datomic.user :as datomic.user]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [auth.wire.token :as wire.token]))

(s/defn service-token! :- wire.token/Token
  [{:keys [auth/service auth/password]} :- wire.auth/ServiceAuthRequest
   config :- protocols.config/IConfig
   crypto :- protocols.crypto/ICrypto]
  (let [{encrypted-password :password scopes :scopes} (protocols.config/get-in! config [:trusted-clients service])]
    (if (protocols.crypto/check crypto encrypted-password password)
      (logic.token/service->token service scopes)
      (exception/forbidden! {:service-token :credentials-do-not-match}))))

(s/defn user-token! :- wire.token/Token
  [{:keys [auth/user-type auth/cred-type] :as auth-request} :- wire.auth/UserAuthRequest
   datomic :- protocols.datomic/IDatomic
   crypto :- protocols.crypto/ICrypto]
  (if (logic.credential/valid-cred-type? user-type cred-type)
    (-> (controllers.credential/authenticate-request! auth-request datomic crypto)
        :credential/user-id
        (datomic.user/lookup! datomic)
        logic.token/user->token)
    (exception/bad-request! {:error :invalid-credential-type})))
