(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh clear]]
            [clojure.pprint :refer [pprint]]
            [clojure.test :as test]
            [clojure.repl :refer (apropos dir doc find-doc pst source)]
            [hamilton.core-test :as hct]
            [hamilton.core :refer [new-router]]
            [hamilton.system :refer [web-system]]
            [com.stuartsierra.component :as component]))

(def system nil)
(defn init []
  (alter-var-root #'system (constantly (web-system {:router (new-router)
                                                    :port 3000}))))
(defn start []
  (alter-var-root #'system component/start))
(defn stop []
  (alter-var-root #'system (fn [s] (when s (component/stop s)))))
(defn go []
  (init)
  (start))
(defn reset []
  (stop)
  (refresh :after 'user/go))
(defn- run-tests-and-go []
  (test/run-tests
   'hamilton.core-test
   'hamilton.controllers-test
   'hamilton.parser-test)
  (go))
(defn t []
  (stop)
  (refresh :after 'user/run-tests-and-go))
