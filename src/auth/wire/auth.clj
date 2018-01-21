(ns auth.wire.auth
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]))

(def service-auth-request-skeleton {:auth/service  {:schema s/Keyword :required true}
                                    :auth/password {:schema s/Str :required true}})
(s/defschema ServiceAuthRequest (schema/skel->schema service-auth-request-skeleton))

(def user-type #{:customer :merchant :carrier})
(s/defschema UserType (apply s/enum user-type))

(def cred-type #{:password :facebook})
(s/defschema CredType (apply s/enum cred-type))

(def user-auth-request-skeleton {:auth/user-type {:schema UserType :required true}
                                 :auth/cred-type {:schema CredType :required true}
                                 :auth/email     {:schema s/Str :require false}
                                 :auth/password  {:schema s/Str :required false}
                                 :auth/fb-id     {:schema s/Str :required false}
                                 :auth/fb-token  {:schema s/Str :required false}})
(s/defschema UserAuthRequest (schema/skel->schema user-auth-request-skeleton))
