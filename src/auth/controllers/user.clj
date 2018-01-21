(ns auth.controllers.user
  (:require [schema.core :as s]
            [auth.logic.token :as logic.token]
            [auth.logic.user :as logic.user]
            [auth.controllers.credential :as controllers.credential]
            [auth.db.datomic.user :as datomic.user]
            [common-labsoft.protocols.token :as protocols.token]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [common-labsoft.protocols.sqs :as protocols.sqs]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [auth.wire.register :as wire.register]
            [auth.logic.credential :as logic.credential]
            [common-labsoft.exception :as exception]))

(s/defn register-new-user! :- s/Str
  [register :- wire.register/Register
   crypto :- protocols.crypto/ICrypto
   token :- protocols.token/IToken
   sqs :- protocols.sqs/ISQS
   datomic :- protocols.datomic/IDatomic]
  (if (logic.credential/valid-cred-type? (:register/type register) (:register/cred-type register))
    (let [user (-> (logic.user/register->user register)
                   (datomic.user/new-user! datomic))]
      (controllers.credential/new-credential! user register crypto datomic)
      (-> (logic.token/user->token user)
          (logic.token/bearer-token token)))
    (exception/bad-request! {:error :invalid-credential-type})))
