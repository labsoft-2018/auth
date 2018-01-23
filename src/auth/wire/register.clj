(ns auth.wire.register
  (:require [schema.core :as s]
            [auth.models.credential :as models.credential]
            [auth.models.user :as models.user]
            [common-labsoft.schema :as schema]))

(def register-skeleton {:register/email     {:schema s/Str :required true}
                        :register/cred-type {:schema models.credential/CredentialType :required true}
                        :register/type      {:schema models.user/UserTypes :required true}
                        :register/password  {:schema s/Str :required false}
                        :register/fb-id     {:schema s/Str :required false}})
(s/defschema Register (schema/skel->schema register-skeleton))
