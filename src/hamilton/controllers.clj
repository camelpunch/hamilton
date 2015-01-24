(ns hamilton.controllers
  (:require [liberator.core :refer [resource]]
            [hamilton.views :as views]))

(defn homepage []
  (resource
   :available-media-types ["text/html"]
   :handle-ok views/homepage))

(defn centrelines [db]
  (resource
   :available-media-types ["application/edn"]
   :handle-ok ["lines"]))
