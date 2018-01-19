(ns auth.aux.test-helpers
  (:require [io.pedestal.http :as http]
            [io.pedestal.test :refer [response-for]]
            [auth.service :as service]
            [cheshire.core :as chesire]
            [datomic.api :as d]))

(defonce ^:private service-fn (atom nil))

(defn test-service
  "Return a service-fn for use with Pedestal's `response-for` test helper."
  []
  (let [system (service/start!)]
    (swap! service-fn #(or % (::http/service-fn (-> system :pedestal :service))))))

(defn with-seeds
  "Return a db with seed tx-data applied"
  ([db tx-data]
   (:db-after (d/with db tx-data))))

(defn request! [service method path & args]
  (-> (apply response-for service method path args)
      :body
      (chesire/parse-string true)))
