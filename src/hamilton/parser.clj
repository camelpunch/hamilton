(ns hamilton.parser
  (:require [clojure.data.zip.xml :refer [xml-> text= text]]))

(defn raw-lines [db waterway]
  (xml-> db
         :gml:featureMember
         :gml2:Canal_Centreline
         [:gml2:SAP_NAME (text= waterway)]
         :gml:lineStringProperty
         text))

(defn split-comma [s] (clojure.string/split s #","))
(defn split-space [s] (clojure.string/split s #" "))

(defn comma-sep-coords [db waterway]
  (map split-space (raw-lines db waterway)))

(defn sections [db waterway]
  (let [comma-sep (comma-sep-coords db waterway)]
    (map (fn [pairs] (map split-comma pairs)) comma-sep)))
