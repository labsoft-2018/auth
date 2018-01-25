(ns auth.wire.auth
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]
            [auth.models.user :as models.user]
            [auth.models.credential :as models.credential]))

(def service-auth-request-skeleton {:auth/service  {:schema s/Keyword :required true}
                                    :auth/password {:schema s/Str :required true}})
(s/defschema ServiceAuthRequest (schema/skel->schema service-auth-request-skeleton))

(def user-auth-request-skeleton {:auth/user-type {:schema models.user/UserTypes :required true}
                                 :auth/cred-type {:schema models.credential/CredentialType :required true}
                                 :auth/email     {:schema s/Str :require false}
                                 :auth/password  {:schema s/Str :required false}
                                 :auth/fb-token  {:schema s/Str :required false}})
(s/defschema UserAuthRequest (schema/skel->schema user-auth-request-skeleton))
