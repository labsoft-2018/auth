(ns auth.controllers.credential
  (:require [schema.core :as s]
            [auth.models.credential :as models.credential]
            [auth.db.datomic.credential :as datomic.credential]
            [auth.logic.credential :as logic.credential]
            [auth.models.user :as models.user]
            [auth.wire.register :as wire.register]
            [auth.diplomat.http :as diplomat.http]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [auth.wire.auth :as wire.auth]
            [common-labsoft.exception :as exception]))

(defmulti prepare-credential! (fn [type _ _] (:credential/type type)))

(s/defmethod prepare-credential! :credential.type/password :- models.credential/Credential
  [credential :- models.credential/Credential
   {:keys [register/password register/email]} :- wire.register/Register
   crypto :- protocols.crypto/ICrypto]
  (->> (protocols.crypto/bcrypt crypto password)
       (assoc credential :credential/encrypted-password)))

(s/defmethod prepare-credential! :credential.type/facebook :- models.credential/Credential
  [credential :- models.credential/Credential
   {:keys [register/fb-id]} :- wire.register/Register
   crypto :- protocols.crypto/ICrypto]
  (assoc credential :credential/fb-id fb-id))

(s/defn new-credential! :- models.credential/Credential
  [{user-id :user/id} :- models.user/User
   {:keys [register/email register/cred-type] :as register} :- wire.register/Register
   crypto :- protocols.crypto/ICrypto
   datomic :- protocols.datomic/IDatomic]
  (-> (logic.credential/base-credential user-id email cred-type)
      (prepare-credential! register crypto)
      (datomic.credential/new-credential! datomic)))

(s/defn authenticate-pass-request! :- models.credential/Credential
  [{:keys [auth/email auth/password]} :- wire.auth/UserAuthRequest
   datomic :- protocols.datomic/IDatomic
   crypto :- protocols.crypto/ICrypto]
  (or (-> (datomic.credential/email->credential email datomic)
          (logic.credential/check-pass-credential password crypto))
      (exception/forbidden! {:error :error-authenticating-credential})))

(s/defn authenticate-fb-request! :- (s/maybe models.credential/Credential)
  [{:keys [auth/fb-token]} :- wire.auth/UserAuthRequest
   datomic :- protocols.datomic/IDatomic
   http]
  (some-> (diplomat.http/fb-token->fb-user! fb-token http)
          :fb-user/id
          (datomic.credential/fb-id->credential datomic)))
