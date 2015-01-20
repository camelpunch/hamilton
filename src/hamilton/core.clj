(ns hamilton.core
  (:require [clojure.java.io :as io]
            [ring.middleware.params :refer [params-request]]
            [bidi.bidi :refer [match-route]]
            [com.stuartsierra.component :as component]
            [hamilton.system :refer [web-system]])
  (:gen-class))

(def routes
  ["/" {"lines" :lines}])

(defn route [handlers request]
  (let [match (match-route routes
                           (:uri request)
                           :request-method (:request-method request))
        key (:handler match)
        handler (key handlers)]
    (handler (params-request request))))

(defn lines [query] {:body "[]"})

(def handlers
  {:lines lines})

(defn new-router [] (partial route handlers))

(defn -main []
  (component/start (web-system {:router (new-router)
                                :port (or (System/getenv "PORT") 3000)})))
