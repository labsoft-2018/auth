(ns auth.logic.credential
  (:require [schema.core :as s]
            [auth.models.credential :as models.credential]))

(s/defn base-credential :- models.credential/Credential
  [user-id :- s/Uuid
   email :- s/Str
   cred-type :- models.credential/CredentialType]
  {:credential/email   email
   :credential/type    cred-type
   :credential/user-id user-id})
