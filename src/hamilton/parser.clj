(ns hamilton.parser
  (:import [uk.me.jstott.jcoord OSRef])
  (:require [clojure.data.zip.xml :refer [xml-> text= text]]))

(defn- raw-lines [db waterway]
  (xml-> db
         :gml:featureMember
         :gml2:Canal_Centreline
         [:gml2:SAP_NAME (text= waterway)]
         :gml:lineStringProperty
         text))

(defn- split-comma [s] (clojure.string/split s #","))
(defn- split-space [s] (clojure.string/split s #" "))

(defn- comma-sep-coords [db waterway]
  (map split-space (raw-lines db waterway)))

(defn- os-sections [db waterway]
  (let [comma-sep (comma-sep-coords db waterway)]
    (map (fn [pairs] (map split-comma pairs)) comma-sep)))

(defn- os2latlng [x y]
  (let [float-x (Float. x)
        float-y (Float. y)
        osref (OSRef. float-x float-y)
        latlng (doto (.toLatLng osref) .toWGS84)]
    {:lat (.getLat latlng)
     :lng (.getLng latlng)}))

(defn sections [db waterway]
    (let [os-secs (os-sections db waterway)]
      (map (fn [os-sec]
             (map #(apply os2latlng %)
                  os-sec))
           os-secs)))
