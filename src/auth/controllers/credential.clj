(ns auth.controllers.credential
  (:require [schema.core :as s]
            [auth.models.credential :as models.credential]
            [auth.db.datomic.credential :as datomic.credential]
            [auth.logic.credential :as logic.credential]
            [auth.models.user :as models.user]
            [auth.wire.register :as wire.register]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [common-labsoft.protocols.datomic :as protocols.datomic]))

(defmulti prepare-credential! (fn [type _ _] (:credential/type type)))

(s/defmethod prepare-credential! :credential.type/password :- models.credential/Credential
  [credential :- models.credential/Credential
   {:keys [register/password register/email]} :- wire.register/Register
   crypto :- protocols.crypto/ICrypto]
  (->> (protocols.crypto/bcrypt crypto password)
       (assoc credential :credential/encrypted-password)))

(s/defn new-credential! :- models.credential/Credential
  [{user-id :user/id} :- models.user/User
   {:keys [register/email register/cred-type] :as register} :- wire.register/Register
   crypto :- protocols.crypto/ICrypto
   datomic :- protocols.datomic/IDatomic]
  (-> (logic.credential/base-credential user-id email cred-type)
      (prepare-credential! register crypto)
      (datomic.credential/new-credential! datomic)))
