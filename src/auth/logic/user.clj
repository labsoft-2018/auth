(ns auth.logic.user
  (:require [schema.core :as s]
            [auth.models.register :as models.register]
            [auth.models.user :as models.user]))

(s/defn register->user :- models.user/User
  [register :- models.register/Register]
  {:user/email (:register/email register)
   :user/type  (:register/type register)})
