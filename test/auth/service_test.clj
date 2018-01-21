(ns auth.service-test
  (:require [midje.sweet :refer :all]
            [common-labsoft.test-helpers :as th]
            [auth.aux.init :refer [test-service]]))

(def service (test-service))

(def pass-register {:register/email     "teste@teste.com"
                    :register/password  "123"
                    :register/cred-type :credential.type/password
                    :register/type      :user.type/customer})

(fact "when registering a new user using password"
  (th/request! service :post "/api/users/register" pass-register) => (contains {:token string?}))
