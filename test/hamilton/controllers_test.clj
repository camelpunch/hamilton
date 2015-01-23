(ns hamilton.controllers-test
  (:require [hamilton.controllers :as controllers]
            [ring.mock.request :as mock :refer [header]]
            [clojure.test :refer :all]))

(defn request [method] (mock/request method "/irrelevant-path"))

(deftest home
  (testing "responds to GET as HTML"
    (is (= "<!DOCTYPE html>" (-> (request :get)
                                 (header "Accept" "text/html")
                                 controllers/homepage
                                 :body
                                 (clojure.string/split #"\n")
                                 first))))
  (testing "405s for POST"
    (is (= 405 (-> (request :post)
                   controllers/homepage
                   :status)))))

(deftest centrelines
  (testing "200 to GET as EDN"
    (is (= 200 (-> (request :get)
                   (header "Accept" "application/edn")
                   controllers/centrelines
                   :status))))
  (testing "406 to GET as HTML"
    (is (= 406 (-> (request :get)
                   (header "Accept" "text/html")
                   controllers/centrelines
                   :status)))))
