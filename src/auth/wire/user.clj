(ns auth.wire.user
  (:require [auth.models.user :as models.user]
            [auth.wire.token :as wire.token]
            [schema.core :as s]
            [common-labsoft.schema :as schema]))

(def authenticated-user-skeleton (merge models.user/user-skeleton
                                        {:user/token {:schema wire.token/Token :required true}}))
(s/defschema AuthenticatedUser (schema/skel->schema authenticated-user-skeleton))

(def user-with-jwt-token (merge models.user/user-skeleton
                                {:user/token {:schema wire.token/JwtBearerToken :required true}}))
(s/defschema UserWithJwtToken (schema/skel->schema user-with-jwt-token))
