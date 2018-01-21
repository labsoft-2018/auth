(ns auth.db.datomic.credential
  (:require [schema.core :as s]
            [auth.models.credential :as models.credential]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [common-labsoft.time :as time]
            [common-labsoft.datomic.api :as datomic.api]))

(s/defn new-credential! :- models.credential/Credential
  [credential :- models.credential/Credential
   datomic :- protocols.datomic/IDatomic]
  (let [full-credential (assoc credential :credential/created-at (time/now))]
    (datomic.api/insert! :credential/id full-credential datomic)))

(s/defn email->credential :- models.credential/Credential
  [email :- s/Str, datomic :- protocols.datomic/IDatomic]
  (datomic.api/query-single! '{:find  [?e]
                               :in    [$ ?email]
                               :where [[?e :credential/email ?email]]}
                             (datomic.api/db datomic) email))
