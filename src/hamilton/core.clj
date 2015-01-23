(ns hamilton.core
  (:require [clojure.java.io :as io]
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

(defn handlers []
  {:homepage (controllers/homepage)
   :centrelines (controllers/centrelines)})

(defn new-router [] (partial route (handlers)))

(defn -main []
  (component/start (web-system {:router (new-router)
                                :port (or (System/getenv "PORT") 3000)})))
