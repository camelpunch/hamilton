(ns hamilton.parser
  (:import [uk.me.jstott.jcoord OSRef])
  (:require [clojure.data.zip.xml :refer [xml-> text= text]]
            [clojure.string :as string]))

(defn- centrelines [db waterway]
  (xml-> db
         :gml:featureMember
         :gml2:Canal_Centreline
         [:gml2:SAP_NAME (text= waterway)]
         :gml:lineStringProperty
         text))

(defn- parse-coords [s]
  (map next (re-seq #"([\d.-]+),([\d.-]+)" s)))

(defn- os2latlng [[x y]]
  (let [osref (OSRef. (Float. x) (Float. y))
        latlng (doto (.toLatLng osref) .toWGS84)]
    {:lat (.getLat latlng)
     :lng (.getLng latlng)}))

(defn sections [db waterway]
  (let [input-sections (map parse-coords (centrelines db waterway))]
    (map (partial map os2latlng) input-sections)))

(defn waterways [db]
  ["Leeds & Liverpool Canal"
   "River Soar Navigation"
   "Tees Navigation"])
