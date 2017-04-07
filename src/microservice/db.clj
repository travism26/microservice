(ns microservice.db
  (:require [clojure.java.jdbc :as j]))


;; Lets try this again....


;; DB Settings.
(def db {:dbtype "postgresql"
         :dbname "micro"
         :host "localhost"
         :user "postgres"
         :password ""})

(defn test [] 
  (j/query db ["select * from usernames"]))

(defn id-for-username
  [input]
  (j/query db ["SELECT user_id from usernames where username =?" input] {:row-fn :user_id :result-set-fn first}))

(defn delete-user
  [username]
  (let [user-id (id-for-username username)
        (j/delete! db :usernames ["user_id = ?" user-id])]))



