(ns auth.models.credential
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]
            [common-labsoft.time :as time]))

(def credential-types #{:credential.type/password :credential.type/facebook})
(s/defschema CredentialType (apply s/enum credential-types))

(def credential-skeleton {:credential/id                 {:schema s/Uuid :id true}
                          :credential/user-id            {:schema s/Uuid :required true}
                          :credential/type               {:schema CredentialType :required true}
                          :credential/email              {:schema s/Str :required true :unique true}
                          :credential/encrypted-password {:schema s/Str :required false}
                          :credential/facebook-id        {:schema s/Str :required false :unique true}
                          :credential/created-at         {:schema time/LocalDateTime :required true}})
(s/defschema Credential (schema/skel->schema credential-skeleton))
