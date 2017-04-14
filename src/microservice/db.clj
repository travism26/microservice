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
  (let [user-id (id-for-username username)]
    (j/delete! db :usernames ["user_id = ?" user-id])))

(defn add-user
  [userdata]
  ; pretend to do input validation here, also sanditize the input (dont know how to
  ; do this yet.
  ; this is my input stream
  ; (jdbc/insert! db :usernames {:username "travism" :first_name "travis" :last_name "martin"})
  (j/insert! db :usernames [userdata])
  )

; TESTING VECs see how to handle input
(def user{:username "john" 
      :first_name "john" 
      :last_name "doe"})

;Testing my eval buffer c-c c-k
(defn hello []
  (println "hello"))

(def load-userdata [
                    ["travismartin" "travis"]
                    ["jonny123" "john"]
                    ["user1" "test"]
                    ["johnson" "jo"]
                    ["hello_user" "mt"]])

(def ud [[:un "epic" :fn "trav" :ln "martin"]
         [:un "eh" :fn "a" :ln "h"]])


(defn load-user-data
  [user]
  (j/insert-multi! db :usernames (map #(hash-map :username (first %1)
                                                 :first_name (second %1)) user)))



(def t {:username "vec" :last_name "lastername" :first_name "ehhe"})

(def gg [["first" "secod" "third"]
         ["hi" "trav" "ironman"]])
(defn ll
  [user]
  j/insert-multi! db :usernames (map #(hash-map :username (first  %1)
                                                :first_name (second %1)
                                                :last_name (last %1)) user))

;different approach 
(defn ll-new
  [user]
  j/insert-multi! db :usernames (map #(hash-map :username (nth %1 0)
                                                :first_name (nth %1 1)
                                                :last_name (nth %1 2)) user))


(defn load-ud
  [user] 
  (j/insert! db :usernames user))

(defn get-user [firstname]
  (j/query db ["select * from usernames where first_name like ? " firstname]))



(defn username-id-remapper
  [m]
  (hash-map (keyword (:username m)) (:user_id m)))


