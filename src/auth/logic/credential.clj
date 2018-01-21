(ns auth.logic.credential
  (:require [schema.core :as s]
            [auth.models.credential :as models.credential]
            [auth.models.user :as models.user]))

(s/defn base-credential :- models.credential/Credential
  [user-id :- s/Uuid
   email :- s/Str
   cred-type :- models.credential/CredentialType]
  {:credential/email   email
   :credential/type    cred-type
   :credential/user-id user-id})

(s/defn valid-cred-type? :- s/Bool
  "Customer can have any kind of credentials. Other users types must use password."
  [user-type :- models.user/UserTypes, cred-type :- models.credential/CredentialType]
  (or (= :user.type/customer user-type)
      (= :credential.type/password cred-type)))
