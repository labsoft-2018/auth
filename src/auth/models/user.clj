(ns auth.models.user
  (:require [schema.core :as s]
            [common-labsoft.schema :as schema]
            [common-labsoft.time :as time]))

(def user-types #{:user.type/customer :user.type/merchant :user.type/carrier})
(s/defschema UserTypes (apply s/enum user-types))

(def user-skeleton {:user/id         {:schema s/Uuid :id true}
                    :user/type       {:schema UserTypes :required true}
                    :user/email      {:schema s/Str :required true}
                    :user/created-at {:schema time/LocalDateTime :required true}})
(s/defschema User (schema/skel->schema user-skeleton))
