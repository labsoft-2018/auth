(ns auth.db.datomic.credential-test
  (:require [midje.sweet :refer :all]
            [common-labsoft.test-helpers :as th]
            [auth.db.datomic.config :as datomic.config]
            [auth.db.datomic.credential :as datomic.credential]
            [common-labsoft.misc :as misc]))

(def created-at #new/time "2018-10-10T12:10:00")
(def user-id (misc/squuid))
(def credential-id (misc/squuid))
(def credential {:credential/user-id            user-id
                 :credential/email              "teste@teste.com"
                 :credential/type               :credential.type/password
                 :credential/encrypted-password "123"})
(def stored-credential (assoc credential :credential/id credential-id
                                         :credential/created-at created-at
                                         :credential/email "tudo@prontaum.com"))

(th/as-of created-at
  (facts "when making operations with credentials"
    (th/with-entities datomic.config/settings [datomic _] [stored-credential]
      (fact "on `new-credential!`"
        (datomic.credential/new-credential! credential datomic)
        => (contains {:credential/user-id            user-id
                      :credential/email              "teste@teste.com"
                      :credential/type               :credential.type/password
                      :credential/encrypted-password "123"
                      :credential/id                 uuid?
                      :credential/created-at         created-at}))

      (fact "on `email->credential`"
        (datomic.credential/email->credential "tudo@prontaum.com" datomic)
        => {:credential/user-id            user-id
            :credential/email              "tudo@prontaum.com"
            :credential/type               :credential.type/password
            :credential/encrypted-password "123"
            :credential/id                 credential-id
            :credential/created-at         created-at}))))
