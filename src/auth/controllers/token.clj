(ns auth.controllers.token
  (:require [schema.core :as s]
            [common-labsoft.protocols.config :as protocols.config]
            [common-labsoft.protocols.token :as protocols.token]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [common-labsoft.exception :as exception]
            [auth.wire.auth :as wire.auth]
            [auth.logic.token :as logic.token]
            [auth.logic.credential :as logic.credential]
            [auth.controllers.credential :as controllers.credential]
            [auth.db.datomic.user :as datomic.user]
            [common-labsoft.protocols.datomic :as protocols.datomic]))

(s/defn service-token! :- s/Str
  [{:keys [auth/service auth/password]} :- wire.auth/ServiceAuthRequest
   config :- protocols.config/IConfig
   token :- protocols.token/IToken
   crypto :- protocols.crypto/ICrypto]
  (let [{encrypted-password :password scopes :scopes} (protocols.config/get-in! config [:trusted-clients service])]
    (if (protocols.crypto/check crypto encrypted-password password)
      (-> (logic.token/service->token service scopes)
          (logic.token/bearer-token token))
      (exception/forbidden! {:service-token :credentials-do-not-match}))))

(s/defn user-token! :- s/Str
  [{:keys [auth/user-type auth/cred-type] :as auth-request} :- wire.auth/UserAuthRequest
   token :- protocols.token/IToken
   datomic :- protocols.datomic/IDatomic
   crypto :- protocols.crypto/ICrypto]
  (if (logic.credential/valid-cred-type? user-type cred-type)
    (-> (controllers.credential/authenticate-request! auth-request datomic crypto)
        :credential/user-id
        (datomic.user/lookup! datomic)
        logic.token/user->token
        (logic.token/bearer-token token))
    (exception/bad-request! {:error :invalid-credential-type})))
