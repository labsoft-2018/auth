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
            [auth.wire.token :as wire.token]
            [auth.logic.user :as logic.user]
            [auth.wire.user :as wire.user]
            [auth.controllers.user :as controllers.user]
            [common-labsoft.protocols.sqs :as protocols.sqs]
            [common-labsoft.protocols.http-client :as protocols.http-client]))

(s/defn service-token! :- wire.token/Token
  [{:keys [auth/service auth/password]} :- wire.auth/ServiceAuthRequest
   config :- protocols.config/IConfig
   crypto :- protocols.crypto/ICrypto]
  (let [{encrypted-password :password scopes :scopes} (protocols.config/get-in! config [:trusted-clients service])]
    (if (protocols.crypto/check crypto encrypted-password password)
      (logic.token/service->token service scopes)
      (exception/forbidden! {:service-token :credentials-do-not-match}))))

(defmulti user-token! (fn [auth-request _ _ _ _] (:auth/cred-type auth-request)))

(s/defmethod user-token! :credential.type/password :- wire.user/AuthenticatedUser
  [{:keys [auth/user-type auth/cred-type] :as auth-request} :- wire.auth/UserAuthRequest
   _ :- protocols.sqs/ISQS
   datomic :- protocols.datomic/IDatomic
   crypto :- protocols.crypto/ICrypto
   http :- protocols.http-client/IHttpClient]
  (-> (controllers.credential/authenticate-pass-request! auth-request datomic crypto)
      (controllers.user/credential->authenticated-user datomic)))

(s/defmethod user-token! :credential.type/facebook :- wire.user/AuthenticatedUser
  [{:keys [auth/user-type auth/cred-type] :as auth-request} :- wire.auth/UserAuthRequest
   sqs :- protocols.sqs/ISQS
   datomic :- protocols.datomic/IDatomic
   crypto :- protocols.crypto/ICrypto
   http :- protocols.http-client/IHttpClient]
  (or (some-> (controllers.credential/authenticate-fb-request! auth-request datomic http)
              (controllers.user/credential->authenticated-user datomic))
      (controllers.user/try-register-facebook-user! auth-request crypto sqs datomic http)))
