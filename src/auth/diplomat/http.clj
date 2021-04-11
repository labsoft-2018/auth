(ns auth.diplomat.http
  (:require [schema.core :as s]
            [common-labsoft.protocols.http-client :as protocols.http]
            [auth.wire.fb-user :as wire.fb-user]
            [auth.logic.facebook :as logic.facebook]
            [auth.adapters.facebook :as adapters.facebook]
            [common-labsoft.adapt :as adapt]))

(s/defn fb-token->fb-user! :- wire.fb-user/FbUser
  [fb-token :- s/Str, http]
  (->> (protocols.http/raw-req! http {:method :get
                                      :accept :json
                                      :url    (logic.facebook/fb-url-for-token fb-token)})
       :body
       (adapt/internalize :json)
       adapters.facebook/external-fb-user->internal-fb-user))
