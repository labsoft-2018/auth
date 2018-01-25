(ns auth.db.datomic.credential-test
  (:require [midje.sweet :refer :all]
            [common-labsoft.test-helpers :as th]
            [auth.db.datomic.config :as datomic.config]
            [auth.db.datomic.credential :as datomic.credential]
            [common-labsoft.misc :as misc]))

(def created-at #time/time "2018-10-10T12:10:00")
(def user-id (misc/squuid))
(def credential-id (misc/squuid))
(def credential-id2 (misc/squuid))
(def credential {:credential/user-id            user-id
                 :credential/type               :credential.type/password
                 :credential/email              "teste@teste.com"
                 :credential/encrypted-password "123"})
(def stored-pass-credential (assoc credential :credential/id credential-id
                                              :credential/created-at created-at
                                              :credential/email "tudo@prontaum.com"))
(def stored-fb-credential {:credential/user-id user-id
                           :credential/type    :credential.type/facebook
                           :credential/id      credential-id2
                           :credential/fb-id    "facebookId"})

(th/as-of created-at
  (facts "when making operations with credentials"
    (th/with-entities datomic.config/settings [datomic _] [stored-pass-credential stored-fb-credential]
      (fact "on `new-credential!`"
        (datomic.credential/new-credential! credential datomic)
        => (contains (assoc credential :credential/created-at created-at)))

      (fact "on `email->credential`"
        (datomic.credential/email->credential "tudo@prontaum.com" datomic) => stored-pass-credential)

      (fact "on `fb-id->credential` - no one is found"
        (datomic.credential/fb-id->credential "123" datomic) => nil)

      (fact "on `fb-id->credential` - we can find one"
        (datomic.credential/fb-id->credential "facebookId" datomic) => stored-fb-credential))))
