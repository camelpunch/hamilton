(ns hamilton.system
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]))

(defrecord WebServer [router port running-server]
  component/Lifecycle

  (start [web-server]
    (println "Starting Jetty")
    (let [jetty (run-jetty router
                           {:port (Integer. port)
                            :join? false})]
      (assoc web-server :running-server jetty)))

  (stop [web-server]
    (println "Stopping Jetty")
    (.stop running-server)
    (assoc web-server :running-server nil)))

(defn new-web-server [router port]
  (map->WebServer {:router router :port port}))

(defn web-system [config]
  (let [{:keys [router port]} config]
    (component/system-map
     :web-server (new-web-server router port))))
