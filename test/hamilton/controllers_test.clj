(ns hamilton.controllers-test
  (:require [hamilton.controllers :refer :all]
            [ring.mock.request :refer :all]
            [clojure.test :refer :all]))

(deftest home
  (testing "responds to GET as HTML"
    (is (= "<!DOCTYPE html>" (-> (request :get "/foo")
                                 (content-type "text/html")
                                 homepage
                                 :body
                                 (clojure.string/split #"\n")
                                 first))))
  (testing "405s for POST"
    (is (= 405 (-> (request :post "/bar")
                   homepage
                   :status)))))
