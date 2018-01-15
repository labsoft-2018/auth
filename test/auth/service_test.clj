(ns auth.service-test
  (:require [midje.sweet :refer :all]
            [auth.aux.test-helpers :as th]))

(def service (th/test-service))

(fact "Http Test"
  (th/request! service :get "/") => {:res "Hello, World!"})
