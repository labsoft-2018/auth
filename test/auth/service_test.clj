(ns auth.service-test
  (:require [midje.sweet :refer :all]
            [common-labsoft.test-helpers :as th]
            [matcher-combinators.midje :refer [match]]
            [matcher-combinators.matchers :as m]
            [auth.service :as service]))

(def pass-register {:register/email     "teste@teste.com"
                    :register/password  "123"
                    :register/cred-type :credential.type/password
                    :register/type      :user.type/customer})
(def service-token-request {:auth/service  "auth"
                            :auth/password "2yRhnZsdXtLYR5FDK0SnAYjfKTuVq6"})

(def user-token-request {:auth/user-type :user.type/customer
                         :auth/cred-type :credential.type/password
                         :auth/email     "teste@teste.com"
                         :auth/password  "123"})

(th/with-service [service/start! service/stop!] [system service]
  (fact "when registering a new user using password"
    (th/request! service :post "/api/users/register" pass-register) => (match
                                                                         (m/contains-map {:user/type  "user.type/customer"
                                                                                          :user/email "teste@teste.com"
                                                                                          :user/token {:token/jwt string?}})))

  (fact "the user can retrieve it's token"
    (th/request! service :post "/api/users/token" user-token-request) => (match
                                                                           (m/contains-map {:user/type  "user.type/customer"
                                                                                            :user/email "teste@teste.com"
                                                                                            :user/token {:token/jwt string?}})))

  (fact "when requesting a service-token"
    (th/request! service :post "/api/services/token" service-token-request) => (contains {:token/jwt string?})))
