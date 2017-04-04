(ns microservice.db
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as sql]
            [microservice.config :as settings]))

(def db-uri
  (java.net.URI. (or 
                  (System/getenv "DATABASE_URL")
                  "postgresql://localhost:5432/DBNAME")))

;; this is my config for the DB connections need to create a env file to load my user/pass and inc to .gitignore. :)
; going to eventually take this hard coded info out and create seperate clj files 
; to easily swap out DBs instead of this ugly way :/
(def db-config
  {
   :classname "org.postgresql.Driver"
   ;:classname "org.h2.Driver"
   :subprotocol "postgresql"
   ;:subprotocol "h2"
   :subname ""
   ;:subname "mem:document" -> in memory database;
   :user "postgres"
   :password ""})

(defn tests
  []
  settings/env)


;; my connection pool (not sure if im doing this the optimal way but oh well, just Testing er out)

