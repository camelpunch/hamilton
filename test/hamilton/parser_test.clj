(ns hamilton.parser-test
  (:require [clojure.test :refer :all]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [hamilton.parser :as parser]))

(defn zip-xml-string [s]
  (-> s
      .getBytes
      java.io.ByteArrayInputStream.
      xml/parse
      zip/xml-zip))

(defn gml-fixture [names-to-coords]
  (let [header "<?xml version='1.0' encoding='UTF-8'?>
<gml2:FeatureCollection
xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
xmlns:gml='http://www.opengis.net/gml'
xmlns:gml2='http://www.safe.com/gml2'
xsi:schemaLocation='http://www.safe.com/gml2 Canal_Centreline.xsd'>"
        footer "</gml2:FeatureCollection>"
        body (reduce (fn [xml [name coords]]
                       (str xml "
<gml:featureMember>
  <gml2:Canal_Centreline>
    <gml2:SAP_NAME>" name "</gml2:SAP_NAME>
    <gml:lineStringProperty>
      <gml:LineString srsName='_BritishNatGrid_0'>
        <gml:coordinates>" coords "</gml:coordinates>
      </gml:LineString>
    </gml:lineStringProperty>
  </gml2:Canal_Centreline>
</gml:featureMember>"))
                     ""
                     names-to-coords)]
    (zip-xml-string (str header body footer))))

(deftest sections
  (testing "sanity of retrieval of a waterway's centreline"
    (let [db (gml-fixture [["Tees Navigation" "1,2"]
                           ["River Soar Navigation" "45,67"]
                           ["Tees Navigation" "5,6 7,8"]])
          sections (parser/sections db "Tees Navigation")
          first-latlng (ffirst sections)]
      (is (= 2 (count sections)))
      (is (= 1 (count (first sections))))
      (is (= 2 (count (second sections))))
      (is (> (:lat first-latlng) 49))
      (is (< (:lat first-latlng) 50))
      (is (> (:lng first-latlng) -8))
      (is (< (:lng first-latlng) -7)))))

(deftest waterways
  (testing "retrieval of all waterway names"
    (let [db (gml-fixture [["Leeds &amp; Liverpool Canal", "0,0"]
                           ["Tees Navigation", "0,0"]
                           ["River Soar Navigation", "0,0"]])
          waterways (parser/waterways db)]
      (is (= ["Leeds & Liverpool Canal"
              "River Soar Navigation"
              "Tees Navigation"] waterways)))))
