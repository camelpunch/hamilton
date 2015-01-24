(ns hamilton.core
  (:require [clojure.java.io :as io]
            [clojure.zip :as zip]
            [clojure.xml :as xml]
            [ring.middleware.params :refer [wrap-params]]
            [bidi.bidi :refer [match-route]]
            [com.stuartsierra.component :as component]
            [hamilton.system :refer [web-system]]
            [hamilton.controllers :as controllers])
  (:gen-class))

(def routes
  ["/" {"" :homepage
        "centrelines" :centrelines}])

(defn- missing-handler [] {:status 500})
(defn- missing-route [] {:status 404})

(defn route [handlers request]
  (let [match (match-route routes (:uri request))]
    (if-let [handler-key (:handler match)]
      (if-let [handler (handler-key handlers)]
        ((wrap-params handler) request)
        (missing-handler))
      (missing-route))))

(defn handlers [centrelines-db]
  {:homepage (controllers/homepage)
   :centrelines (controllers/centrelines centrelines-db)})

(defn- centreline-zip []
  (-> "Canal_Centreline.gml" io/file xml/parse zip/xml-zip))
(defn new-router [] (partial route (handlers (centreline-zip))))
(defn -main []
  (component/start (web-system {:router (new-router)
                                :port (or (System/getenv "PORT") 3000)})))
