(ns auth.service-test
  (:require [midje.sweet :refer :all]
            [common-labsoft.test-helpers :as th]
            [auth.service :as service]))

(def pass-register {:register/email     "teste@teste.com"
                    :register/password  "123"
                    :register/cred-type :credential.type/password
                    :register/type      :user.type/customer})
(def service-token-request {:auth/service "auth"
                            :auth/password "123"})

(th/with-service [service/start! service/stop!] [system service]
  (fact "when registering a new user using password"
    (th/request! service :post "/api/users/register" pass-register) => (contains {:token/jwt string?}))

  (fact "when requesting a service-token"
    (th/request! service :post "/api/services/token" service-token-request) => (contains {:token/jwt string?})))
