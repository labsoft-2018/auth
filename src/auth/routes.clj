(ns auth.routes
  (:require [common-labsoft.pedestal.interceptors.auth :as int-auth]
            [common-labsoft.pedestal.interceptors.error :as int-err]
            [common-labsoft.pedestal.interceptors.schema :as int-schema]
            [auth.adapters.token :as adapters.token]
            [auth.controllers.user :as controllers.user]
            [auth.controllers.token :as controllers.token]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [auth.wire.register :as wire.register]
            [auth.wire.auth :as wire.auth]
            [auth.wire.token :as wire.token]
            [auth.models.user :as models.user]))

(defn register-user
  [{{:keys [sqs datomic crypto]} :components register :data}]
  {:status 200
   :schema models.user/User
   :body   (controllers.user/register-new-user! register crypto sqs datomic)})

(defn service-token
  [{{:keys [crypto token config]} :components auth-request :data}]
  {:status 200
   :schema wire.token/JwtBearerToken
   :body   (-> (controllers.token/service-token! auth-request config crypto)
               (adapters.token/token->jwt-token token))})

(defn user-token
  [{{:keys [crypto token datomic]} :components auth-request :data}]
  {:status 200
   :schema wire.token/JwtBearerToken
   :body   (-> (controllers.token/user-token! auth-request datomic crypto)
               (adapters.token/token->jwt-token token))})

(defroutes routes
  [[["/" ^:interceptors [int-err/catch!
                         (body-params/body-params)
                         http/json-body
                         int-auth/auth
                         int-schema/coerce-output]
     ["/api"

      ["/users"
       ["/register" ^:interceptors [(int-schema/coerce wire.register/Register)]
        {:post [:user-register register-user]}]

       ["/token" ^:interceptors [(int-schema/coerce wire.auth/UserAuthRequest)]
        {:post [:user-token user-token]}]]

      ["/services/token" ^:interceptors [(int-schema/coerce wire.auth/ServiceAuthRequest)]
       {:post [:service-token service-token]}]]]]])
