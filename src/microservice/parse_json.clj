(ns microservice.parse_json
  (:require [cheshire.core :refer :all]
            [microservice.db :as db]))

;microservice.service
(defn to-json-string
  [data]
  "this is to parse the db results"
  (generate-string data))

(def db-call {:user_id 6, :username "travismartin", :first_name "travis", :last_name nil})

;; generate some json
(defn parse-values [data] (generate-string {:foo "bar" :baz 5}))
