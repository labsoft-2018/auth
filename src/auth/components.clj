(ns auth.components
  (:require [com.stuartsierra.component :as component]
            [auth.routes :refer [routes]]
            [auth.diplomat.sqs :as sqs]
            [auth.db.datomic.config :as datomic.config]
            [common-labsoft.components.webapp :as components.webapp]
            [common-labsoft.components.crypto :as components.crypto]
            [common-labsoft.components.token :as components.token]
            [common-labsoft.components.datomic :as components.datomic]
            [common-labsoft.components.pedestal :as components.pedestal]
            [common-labsoft.components.s3-client :as components.s3-client]
            [common-labsoft.components.config :as components.config]
            [common-labsoft.components.sqs :as components.sqs]))

(defn base-system [config-name]
  (component/system-map
    :config (component/using (components.config/new-config config-name) [])
    :s3-auth (component/using (components.s3-client/new-s3-client :s3-auth) [:config])
    :pedestal (component/using (components.pedestal/new-pedestal routes) [:config :webapp])
    :datomic (component/using (components.datomic/new-datomic datomic.config/settings) [:config])
    :token (component/using (components.token/new-token) [:config :s3-auth])
    :crypto (component/using (components.crypto/new-crypto) [:config])
    :sqs-producer (component/using (components.sqs/new-producer sqs/settings) [:config])
    :sqs-consumer (component/using (components.sqs/new-consumer sqs/settings) [:config :webapp])
    :webapp (component/using (components.webapp/new-webapp) [:config :datomic :token :crypto :sqs-producer])))
