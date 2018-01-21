(ns auth.aux.init
  (:require [io.pedestal.http :as http]
            [io.pedestal.test :refer [response-for]]
            [auth.service :as service]))

(defonce ^:private service-fn (atom nil))

(defn test-service
  "Return a service-fn for use with Pedestal's `response-for` test helper."
  []
  (let [system (service/restart!)]
    (reset! service-fn (::http/service-fn (-> system :pedestal :service)))))
