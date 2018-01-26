(ns auth.wire.fb-user
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]))

(def fb-user-skeleton {:fb-user/id          {:schema s/Str :required true}
                       :fb-user/email       {:schema s/Str :required true}
                       :fb-user/name        {:schema s/Str :required true}
                       :fb-user/picture-url {:scehma s/Str :required true}})
(s/defschema FbUser (schema/skel->schema fb-user-skeleton))
