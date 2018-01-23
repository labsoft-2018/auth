(ns auth.adapters.facebook
  (:require [schema.core :as s]
            [auth.wire.fb-user :as wire.fb-user]
            [auth.wire.register :as wire.register]))

(s/defn fb-user->register :- wire.register/Register
  [fb-user :- wire.fb-user/FbUser]
  {:register/type      :user.type/customer
   :register/cred-type :credential.type/facebook
   :register/email     (:fb-user/email fb-user)
   :register/fb-id     (:fb-user/id fb-user)})
