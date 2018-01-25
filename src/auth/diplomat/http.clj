(ns auth.diplomat.http
  (:require [schema.core :as s]
            [auth.wire.fb-user :as wire.fb-user]))

(s/defn fb-token->fb-user! :- wire.fb-user/FbUser
  [fb-token :- s/Str, http]
  )
