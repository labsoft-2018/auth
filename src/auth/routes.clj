(ns auth.routes
  (:require [common-labsoft.pedestal.interceptors.auth :as int-auth]
            [common-labsoft.pedestal.interceptors.error :as int-err]
            [common-labsoft.pedestal.interceptors.schema :as int-schema]
            [auth.controllers.user :as controllers.user]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [schema.core :as s]
            [auth.models.register :as models.register]))

(defn hello-world
  [request]
  {:status 200
   :body   {:res "Hello, World!"}})

(defn register-user
  [{{:keys [sqs datomic crypto token]} :components register :data :as request}]
  (prn register)
  {:status 200
   :body {:token (controllers.user/register-new-user! register crypto token sqs datomic)}})

(defroutes routes
           [[["/" ^:interceptors [int-err/catch!
                                  (body-params/body-params)
                                  http/json-body
                                  int-auth/auth
                                  int-schema/coerce-output]
              ["/api"
               {:get [:hello-world hello-world]}

               ["/users/register" ^:interceptors [(int-schema/coerce models.register/Register)]
                {:post [:register register-user]}]]]]])
