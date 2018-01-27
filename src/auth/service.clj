(ns auth.service
  (:gen-class)
  (:require [common-labsoft.system :as system]
            [schema.core]
            [auth.components :as components]))

(defn start! [& args]
  (-> (components/base-system "dev_config.json")
      (system/bootstrap!)))

(defn stop! []
  (system/stop!))

(defn restart! []
  (stop!)
  (start!))

(defn -main [& args]
  (-> (components/base-system "prod_config.json")
      (system/bootstrap!)))
