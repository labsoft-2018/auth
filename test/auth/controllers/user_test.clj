(ns auth.controllers.user-test
  (:require [midje.sweet :refer :all]
            [auth.controllers.user :as controllers.user]
            [auth.logic.user :as logic.user]
            [auth.db.datomic.user :as datomic.user]
            [auth.controllers.credential :as controllers.credential]
            [auth.logic.token :as logic.token]))

(facts "when registering a new user"
  (fact "on `register-new-user!`"
    (controllers.user/register-new-user! ..register.. ..crypto.. ..token.. ..sqs.. ..datomic..) => ..bearer-token..
    (provided
      (logic.user/register->user ..register..) => ..user..
      (datomic.user/new-user! ..user.. ..datomic..) => ..stored-user..
      (controllers.credential/new-credential! ..stored-user.. ..register.. ..crypto.. ..datomic..) => ..credential..
      (logic.token/user->token ..stored-user..) => ..user-token..
      (logic.token/bearer-token ..user-token.. ..token..) => ..bearer-token..)))
