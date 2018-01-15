(ns auth.service
  (:gen-class)
  (:require [common-labsoft.system :as system]
            [auth.routes :refer [routes]]))

(defn start [& args]
  (system/bootstrap! {:config-name "dev_config.json"
                      :routes routes}))
(defn stop []
  (system/stop!))

(defn restart []
  (stop)
  (start))

(defn -main [& args]
  (system/bootstrap! {:config-name "prod_config.json"
                      :routes routes}))
