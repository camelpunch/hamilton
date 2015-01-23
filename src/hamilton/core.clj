(ns hamilton.core
  (:require [clojure.java.io :as io]
            [ring.middleware.params :refer [params-request]]
            [bidi.bidi :refer [match-route]]
            [com.stuartsierra.component :as component]
            [hamilton.system :refer [web-system]]
            [hamilton.controllers :as controllers])
  (:gen-class))

(def routes
  ["/" {"" :homepage
        "lines" :lines}])

(defn route [handlers request]
  (let [match (match-route routes
                           (:uri request)
                           :request-method (:request-method request))
        key (:handler match)]
    (if-let [key (:handler match)]
      (let [handler (key handlers)]
        (handler (params-request request)))
      {:status 404})))

(defn handlers []
  {:homepage controllers/homepage
   :lines controllers/lines})

(defn new-router [] (partial route (handlers)))

(defn -main []
  (component/start (web-system {:router (new-router)
                                :port (or (System/getenv "PORT") 3000)})))
