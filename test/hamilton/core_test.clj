(ns hamilton.core-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer [request]]
            [hamilton.core :refer :all]))

(deftest wiring
  (testing "GET /centrelines?q=Tees+Navigation returns 200"
    (let [req (request :get "/centrelines?q=Tees+Navigation")
          res ((new-router) req)]
      (is (= 200 (:status res)) (:body res))))
  (testing "GET /waterways returns 200"
    (let [req (request :get "/waterways")
          res ((new-router) req)]
      (is (= 200 (:status res)) (:body res)))))

(deftest routing
  (testing "GET /centrelines calls the :centrelines handler with given params"
    (let [q-from-req (fn [req] {:body (get-in req [:params "q"])})
          handlers {:centrelines q-from-req}
          req (request :get "/centrelines?q=Shropshire+Union")
          res (route handlers req)]
      (is (= "Shropshire Union" (:body res)))))

  (testing "GET /waterways calls the :waterways handler"
    (let [i-got-called (fn [req] {:body "I got called"})
          handlers {:waterways i-got-called}
          req (request :get "/waterways")
          res (route handlers req)]
      (is (= "I got called" (:body res)))))

  (testing "404 on non-existent route"
    (let [handlers {}
          req {:uri "/madeupplace"
               :query-string nil}
          res (route handlers req)]
      (is (= 404 (:status res)))))

  (testing "500 when route in place but handler not"
    (let [handlers {}
          req (request :get "/centrelines")
          res (route handlers req)]
      (is (= 500 (:status res))))))
