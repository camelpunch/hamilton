(ns hamilton.controllers
  (:require [liberator.core :refer [resource]]
            [hamilton.views :as views]))

(defn homepage []
  (resource
   :available-media-types ["text/html"]
   :handle-ok views/homepage))

(defn centrelines [parse-fn]
  (resource
   :available-media-types ["application/json"]
   :exists? (fn [ctx]
              (let [waterway (get-in (:request ctx)
                                     [:query-params "q"])
                    sections (parse-fn waterway)]
                (if-not (empty? sections)
                  {:sections sections})))
   :handle-ok (fn [ctx]
                (apply concat (:sections ctx)))))
