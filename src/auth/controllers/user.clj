(ns auth.controllers.user
  (:require [schema.core :as s]
            [auth.logic.user :as logic.user]
            [auth.diplomat.http :as diplomat.http]
            [auth.adapters.facebook :as adapters.facebook]
            [auth.controllers.credential :as controllers.credential]
            [auth.db.datomic.user :as datomic.user]
            [common-labsoft.protocols.crypto :as protocols.crypto]
            [common-labsoft.protocols.sqs :as protocols.sqs]
            [common-labsoft.protocols.datomic :as protocols.datomic]
            [auth.wire.register :as wire.register]
            [auth.logic.credential :as logic.credential]
            [common-labsoft.exception :as exception]
            [auth.wire.user :as wire.user]
            [auth.wire.auth :as wire.auth]
            [auth.models.credential :as models.credential]))

(s/defn register-new-user! :- wire.user/AuthenticatedUser
  [register :- wire.register/Register
   crypto :- protocols.crypto/ICrypto
   sqs :- protocols.sqs/ISQS
   datomic :- protocols.datomic/IDatomic]
  (if (logic.credential/valid-cred-type? (:register/type register) (:register/cred-type register))
    (let [user (-> (logic.user/register->user register)
                   (datomic.user/new-user! datomic))]
      (controllers.credential/new-credential! user register crypto datomic)
      (logic.user/user->authenticated-user user))
    (exception/bad-request! {:error :invalid-credential-type})))

(s/defn credential->authenticated-user :- wire.user/AuthenticatedUser
  [credential :- models.credential/Credential, datomic :- protocols.datomic/IDatomic]
  (-> (:credential/user-id credential)
      (datomic.user/lookup! datomic)
      logic.user/user->authenticated-user))

(s/defn try-register-facebook-user! :- wire.user/AuthenticatedUser
  [{:keys [auth/fb-token auth/user-type]} :- wire.auth/UserAuthRequest
   crypto :- protocols.crypto/ICrypto
   sqs :- protocols.sqs/ISQS
   datomic :- protocols.datomic/IDatomic
   http]
  (or (and (logic.credential/valid-cred-type? user-type :credential.type/facebook)
           (some-> (diplomat.http/fb-token->fb-user! fb-token http)
                   (adapters.facebook/fb-user->register)
                   (register-new-user! crypto sqs datomic)))
      (exception/bad-request! {:error :error-while-registering-with-facebook})))
