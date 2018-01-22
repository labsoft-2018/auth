(ns auth.db.datomic.user
  (:require [common-labsoft.datomic.api :as datomic.api]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [auth.models.user :as models.user]
            [schema.core :as s]
            [common-labsoft.time :as time]))

(s/defn new-user! :- models.user/User
  [user :- models.user/User, datomic :- protocols.datomic/IDatomic]
  (let [prepared-user (assoc user :user/created-at (time/now))]
    (datomic.api/insert! :user/id prepared-user datomic)))

(s/defn lookup! :- models.user/User
  [user-id :- s/Uuid, datomic :- protocols.datomic/IDatomic]
  (datomic.api/lookup! :user/id user-id (datomic.api/db datomic)))
