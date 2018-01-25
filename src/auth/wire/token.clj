(ns auth.wire.token
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]))

(def token-types #{:customer :merchant :carrier :service})
(s/defschema TokenType (apply s/enum token-types))

(def token-skeleton {:token/sub    {:schema s/Str :required true}
                     :token/type   {:schema TokenType :required true}
                     :token/scopes {:schema #{s/Str} :required true}})
(s/defschema Token (schema/skel->schema token-skeleton))

(def jwt-bearer-skeleton {:token/jwt {:schema s/Str :required true}})
(s/defschema JwtBearerToken (schema/skel->schema jwt-bearer-skeleton))
