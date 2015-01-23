(ns hamilton.core
  (:require [clojure.java.io :as io]
            [ring.middleware.params :refer [wrap-params]]
            [bidi.bidi :refer [match-route]]
            [liberator.core :refer [resource defresource]]
            [com.stuartsierra.component :as component]
            [hamilton.system :refer [web-system]]
            [hamilton.controllers :as controllers])
  (:gen-class))

(def routes
  ["/" {"" :homepage
        "lines" :lines}])

(defn route [handlers request]
  (let [match (match-route routes (:uri request))]
    (if-let [handler-key (:handler match)]
      (if-let [handler (handler-key handlers)]
        ((wrap-params handler) request)
        {:status 500})
      {:status 404})))

(defn handlers []
  {:homepage (resource)
   :lines (resource)})

(defn new-router [] (partial route (handlers)))

(defn -main []
  (component/start (web-system {:router (new-router)
                                :port (or (System/getenv "PORT") 3000)})))
