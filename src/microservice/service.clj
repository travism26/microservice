(ns microservice.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
                                        ;[io.pedestal.http :as bootstrap]
            [ring.util.response :as ring-resp]
            [microservice.db :as db]
            [cheshire.core :as core]
            [cheshire.factory :as factory]
            [microservice.parse_json :as parser]))

(def mock-project-collection
  {
   :sleeping-cat
   {
    :name "some name here"
    :framework "pedestal"
    :language "clojure"
    :repo "git"
    }
   :stinky-dog
   {
    :name "second name"
    :framework "grails"
    :language "groovy"
    :repo "bitbucket"
    }
   })

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn home-page
  [request]
  ;; this just prints the request map.
  (prn request)
  (ring-resp/response (parser/to-json-string (db/test)) ))

(defn get-projects
  [request]
  (http/json-response mock-project-collection)
  )

(defn get-project
  [request]
  (prn request)
  ;WTF is going on throwing an NPE??! fix this tmw
  (let [projname (get-in request [:path-params :project-name])]
    (prn "this is a sep")
    (prn projname)
    (http/json-response ((keyword projname) mock-project-collection))
    )
  )

; need to fix this to post to a DB
; need to add a db.
(defn add-project
  [request]
  (prn (:json-params request))
  (ring-resp/created "http://fake-201-url" "fake 201 in the body")
  )

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/about" :get (conj common-interceptors `about-page)]
              ["/projects" :get (conj common-interceptors `get-projects)]
              ["/projects" :post (conj common-interceptors `add-project)]
              ["/projects/:project-name" :get (conj common-interceptors `get-project)]
              })

;; Map-based routes
;(def routes `{"/" {:interceptors [(body-params/body-params) http/html-body]
;                   :get home-page
;                   "/about" {:get about-page}}})

;; Terse/Vector-based routes
;(def routes
;  `[[["/" {:get home-page}
;      ^:interceptors [(body-params/body-params) http/html-body]
;      ["/about" {:get about-page}]]]])


;; Consumed by microservice.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ::http/type :jetty
              ;;::http/host "localhost"
              ::http/port (Integer. (or (System/getenv "PORT") 8080))
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false}})

