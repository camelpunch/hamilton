(ns hamilton.controllers
  (:require [liberator.core :refer [defresource]]
            [hamilton.views :as views]))

(defresource homepage
  :available-media-types ["text/html"]
  :handle-ok views/homepage)

(defresource centrelines
  :available-media-types ["application/edn"]
  :handle-ok ["lines"])
