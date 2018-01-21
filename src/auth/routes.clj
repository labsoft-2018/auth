(ns auth.routes
  (:require [common-labsoft.pedestal.interceptors.auth :as int-auth]
            [common-labsoft.pedestal.interceptors.error :as int-err]
            [common-labsoft.pedestal.interceptors.schema :as int-schema]
            [auth.controllers.user :as controllers.user]
            [auth.controllers.token :as controllers.token]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [schema.core :as s]
            [auth.wire.register :as wire.register]
            [auth.wire.auth :as wire.auth]
            [auth.wire.token :as wire.token]))

(defn hello-world
  [request]
  {:status 200
   :body   {:res "Hello, World!"}})

(defn register-user
  [{{:keys [sqs datomic crypto token]} :components register :data}]
  {:status 200
   :schema wire.token/JwtBearerToken
   :body   {:token/jwt (controllers.user/register-new-user! register crypto token sqs datomic)}})

(defn service-token
  [{{:keys [crypto token config]} :components auth-request :data}]
  {:status 200
   :schema wire.token/JwtBearerToken
   :body   {:token/jwt (controllers.token/service-token! auth-request config token crypto)}})

(defroutes routes
  [[["/" ^:interceptors [int-err/catch!
                         (body-params/body-params)
                         http/json-body
                         int-auth/auth
                         int-schema/coerce-output]
     ["/api"
      {:get [:hello-world hello-world]}

      ["/users/register" ^:interceptors [(int-schema/coerce wire.register/Register)]
       {:post [:user-register register-user]}]

      ["/services/token" ^:interceptors [(int-schema/coerce wire.auth/ServiceAuthRequest)]
       {:post [:service-token service-token]}]]]]])
