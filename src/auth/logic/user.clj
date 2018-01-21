(ns auth.logic.user
  (:require [schema.core :as s]
            [auth.wire.register :as wire.register]
            [auth.models.user :as models.user]))

(s/defn register->user :- models.user/User
  [register :- wire.register/Register]
  {:user/email (:register/email register)
   :user/type  (:register/type register)})
