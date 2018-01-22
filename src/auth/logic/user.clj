(ns auth.logic.user
  (:require [schema.core :as s]
            [auth.wire.register :as wire.register]
            [auth.wire.user :as wire.user]
            [auth.models.user :as models.user]
            [auth.logic.token :as logic.token]))

(s/defn register->user :- models.user/User
  [register :- wire.register/Register]
  {:user/email (:register/email register)
   :user/type  (:register/type register)})

(s/defn user->authenticated-user :- wire.user/AuthenticatedUser
  [user :- models.user/User]
  (assoc user :user/token (logic.token/user->token user)))
