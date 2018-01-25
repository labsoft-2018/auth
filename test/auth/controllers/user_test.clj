(ns auth.controllers.user-test
  (:require [midje.sweet :refer :all]
            [auth.controllers.user :as controllers.user]
            [auth.logic.user :as logic.user]
            [auth.db.datomic.user :as datomic.user]
            [auth.controllers.credential :as controllers.credential]
            [auth.logic.credential :as logic.credential]
            [auth.diplomat.http :as diplomat.http]
            [auth.adapters.facebook :as adapters.facebook]))

(facts "when registering a new user"
  (fact "on `register-new-user!` - a valid credential is given"
    (controllers.user/register-new-user! ..register.. ..crypto.. ..sqs.. ..datomic..) => ..authenticated-user..
    (provided
      ..register.. =contains=> {:register/cred-type ..cred-type.. :register/type ..user-type..}
      (logic.credential/valid-cred-type? ..user-type.. ..cred-type..) => true
      (logic.user/register->user ..register..) => ..user..
      (datomic.user/new-user! ..user.. ..datomic..) => ..stored-user..
      (controllers.credential/new-credential! ..stored-user.. ..register.. ..crypto.. ..datomic..) => ..credential..
      (logic.user/user->authenticated-user ..stored-user..) => ..authenticated-user..))

  (fact "on `register-new-user!` - a INVALID credential is given"
    (controllers.user/register-new-user! ..register.. ..crypto.. ..sqs.. ..datomic..) => (throws Exception)
    (provided
      ..register.. =contains=> {:register/cred-type ..cred-type.. :register/type ..user-type..}
      (logic.credential/valid-cred-type? ..user-type.. ..cred-type..) => false))

  (fact "on `try-register-facebook-user!`"
    (controllers.user/try-register-facebook-user! {:auth/fb-token ..fb-token..
                                                   :auth/user-type ..user-type..} ..crypto.. ..sqs.. ..datomic.. ..http..)
    => ..authenticated-user..
    (provided
      (logic.credential/valid-cred-type? ..user-type.. :credential.type/facebook) => true
      (diplomat.http/fb-token->fb-user! ..fb-token.. ..http..) => ..fb-user..
      (adapters.facebook/fb-user->register ..fb-user..) => ..register..
      (controllers.user/register-new-user! ..register.. ..crypto.. ..sqs.. ..datomic..) => ..authenticated-user..)))

(facts "getting an authenticated user from a credential"
  (fact "on `credential->authenticated-user`"
    (controllers.user/credential->authenticated-user {:credential/user-id ..user-id..} ..datomic..) => ..authenticated-user..
    (provided
      (datomic.user/lookup! ..user-id.. ..datomic..) => ..user..
      (logic.user/user->authenticated-user ..user..) => ..authenticated-user..)))


