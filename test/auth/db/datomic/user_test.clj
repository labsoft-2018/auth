(ns auth.db.datomic.user-test
  (:require [midje.sweet :refer :all]
            [auth.db.datomic.user :as datomic.user]
            [common-labsoft.test-helpers :as th]
            [common-labsoft.misc :as misc]
            [auth.db.datomic.config :as datomic.config]))

(def user-id (misc/squuid))
(def user {:user/email "test@test.com"
           :user/type  :user.type/customer})
(def created-at #new/time "2018-10-10T12:10:00")
(def stored-user (assoc user :user/id user-id
                             :user/created-at created-at))

(th/as-of created-at
  (facts "when making operations to the user model"
    (th/with-entities datomic.config/settings [datomic db] []
      (fact "on `new-user!`"
        (datomic.user/new-user! user datomic) => (contains {:user/email      "test@test.com"
                                                            :user/created-at created-at
                                                            :user/type       :user.type/customer
                                                            :user/id         uuid?})))

    (th/with-entities datomic.config/settings [datomic db] [stored-user]
      (fact "on `lookup!`"
        (datomic.user/lookup! user-id datomic) => stored-user))))
