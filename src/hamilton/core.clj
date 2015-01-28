(ns hamilton.core
  (:require [clojure.java.io :as io]
            [clojure.zip :as zip]
            [clojure.xml :as xml]
            [ring.middleware.params :refer [wrap-params]]
            [bidi.bidi :refer [match-route]]
            [com.stuartsierra.component :as component]
            [hamilton.system :refer [web-system]]
            [hamilton.controllers :as controllers]
            [hamilton.parser :as parser])
  (:gen-class))

(def ^:private missing-handler {:status 500})
(def ^:private missing-route {:status 404})
(def ^:private centreline-zip
  (-> "Canal_Centreline.gml" io/file xml/parse zip/xml-zip))

(defn- process-request [req handler]
  (if handler
    ((wrap-params handler) req)
    missing-handler))

(def routes
  ["/" {"" :homepage
        "centrelines" :centrelines}])

(defn route [handlers req]
  (let [match (match-route routes (:uri req))]
    (if-let [handler-key (:handler match)]
      (process-request req (handler-key handlers))
      missing-route)))

(defn- handlers [centrelines-db]
  {:homepage (controllers/homepage)
   :centrelines (controllers/centrelines
                 (partial parser/sections centrelines-db))})

(defn new-router [] (partial route (handlers centreline-zip)))
(defn -main []
  (component/start (web-system {:router (new-router)
                                :port (or (System/getenv "PORT") 3000)})))
