(ns auth.wire.auth
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]))

(def service-auth-request-skeleton {:auth/service  {:schema s/Keyword :required true}
                                    :auth/password {:schema s/Str :required true}})
(s/defschema ServiceAuthRequest (schema/skel->schema service-auth-request-skeleton))
