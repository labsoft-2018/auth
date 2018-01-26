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


(s/defn external-fb-user->internal-fb-user :- wire.fb-user/FbUser
  [external-fb-user]
  {:fb-user/id          (:id external-fb-user)
   :fb-user/email       (or (:email external-fb-user) "unknwon@unknown.com")  ; FIX THIS
   :fb-user/name        (:name external-fb-user)
   :fb-user/picture-url (-> external-fb-user :picture :url)})