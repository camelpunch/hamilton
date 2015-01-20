(ns hamilton.core-test
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [hamilton.core :refer :all]))

(deftest routing
  (testing "/lines calls the :lines handler with given params"
    (let [handlers {:lines (fn [req] (:query-params req))}
          sent-params (->> {:uri "/lines"
                            :query-string "q=Shropshire Union"}
                           (route handlers))]
      (is (= "Shropshire Union" (sent-params "q"))))))
