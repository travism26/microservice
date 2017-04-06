(ns microservice.db
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as j]
            [microservice.config :as settings]))


;; Lets try this again....


;; DB Settings.
(def db {:dbtype "postgresql"
         :dbname "micro"
         :host "localhost"
         :user "postgres"
         :password ""})

(defn test [] 
  (j/query db ["select * from usernames"]))
