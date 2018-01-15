(ns auth.routes
  (:require [common-labsoft.pedestal.interceptors.auth :as int-auth]
            [common-labsoft.pedestal.interceptors.error :as int-err]
            [common-labsoft.pedestal.interceptors.schema :as int-schema]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]))

(defn hello-world
  [request]
  {:status 200
   :body   {:res "Hello, World!"}})

(defroutes routes
           [[["/" ^:interceptors [int-err/catch!
                                  (body-params/body-params)
                                  http/json-body
                                  int-auth/auth
                                  int-schema/coerce-output]
              {:get [:hello-world hello-world]}]]])
