(ns auth.controllers.user-test
  (:require [midje.sweet :refer :all]
            [auth.controllers.user :as controllers.user]
            [auth.logic.user :as logic.user]
            [auth.db.datomic.user :as datomic.user]
            [auth.controllers.credential :as controllers.credential]
            [auth.logic.credential :as logic.credential]))

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
      (logic.credential/valid-cred-type? ..user-type.. ..cred-type..) => false)))
