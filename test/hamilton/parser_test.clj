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
  (testing "retrieval of a waterway's centreline"
    (let [db (gml-fixture [["Tees Navigation" "1,2 3,4"]
                           ["River Soar Navigation" "45,67 89,10"]
                           ["Tees Navigation" "5,6 7,8"]])]
      (is (= [[["1" "2"]
               ["3" "4"]]
              [["5" "6"]
               ["7" "8"]]]
             (parser/sections db "Tees Navigation"))))))
