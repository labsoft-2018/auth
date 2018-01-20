(ns auth.db.datomic.config
  (:require [auth.models.user :as models.user]
            [auth.models.credential :as models.credential]))

(def settings {:schemas [models.user/user-skeleton
                         models.credential/credential-skeleton]
               :enums   [models.user/user-types
                         models.credential/credential-types]})