(ns auth.models.token
  (:require [schema.core :as s]
            [auth.models.user :as models.user]
            [common-labsoft.schema :as schema]))

(def token-skeleton {:token/user-id   {:schema s/Uuid :required true}
                     :token/user-type {:schema models.user/UserTypes :required true}
                     :token/scopes    {:schema #{s/Str} :required true}})
(s/defschema Token (schema/skel->schema token-skeleton))