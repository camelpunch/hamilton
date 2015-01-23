(ns hamilton.core-test
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [ring.mock.request :refer :all]
            [hamilton.core :refer :all]))

(deftest routing
  (testing "GET /lines calls the :lines handler with given params"
    (let [q-from-req (fn [req] {:body (get-in req [:params "q"])})
          handlers {:lines q-from-req}
          req (request :get "/lines?q=Shropshire+Union")
          res (route handlers req)]
      (is (= "Shropshire Union" (:body res)))))

  (testing "404 on non-existent route"
    (let [handlers {}
          req {:uri "/madeupplace"
               :query-string nil}
          res (route handlers req)]
      (is (= 404 (:status res)))))

  (testing "500 when route in place but handler not"
    (let [handlers {}
          req (request :get "/lines")
          res (route handlers req)]
      (is (= 500 (:status res))))))
