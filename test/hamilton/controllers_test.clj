(ns hamilton.controllers-test
  (:require [hamilton.controllers :as controllers]
            [ring.mock.request :as mock :refer [header]]
            [ring.middleware.params :refer [params-request]]
            [clojure.data.json :as json]
            [clojure.test :refer :all]))

(defn request [method] (mock/request method "/irrelevant-path"))

(deftest home
  (let [handler (controllers/homepage)]
    (testing "responds to GET as HTML"
      (is (= "<!DOCTYPE html>" (-> (request :get)
                                   (header "Accept" "text/html")
                                   handler
                                   :body
                                   (clojure.string/split #"\n")
                                   first))))
    (testing "405s for POST"
      (is (= 405 (-> (request :post) handler :status))))))

(deftest centrelines
  (let [handler (controllers/centrelines identity)]
    (testing "405s for POST"
      (is (= 405 (-> (request :post) handler :status))))))

(deftest centrelines-with-match
  (let [f (fn [waterway]
            (when (= waterway "Tees Navigation")
              [[["456" "789"]
                ["101" "121"]]
               [["314" "151"]]]))
        handler (controllers/centrelines f)
        req (-> (params-request (mock/request :get "/?q=Tees+Navigation"))
                (header "Accept" "application/json"))]
    (testing "responds with 200"
      (is (= 200 (-> req handler :status))))
    (testing "body contains concatenated coords"
      (is (= [["456" "789"]
              ["101" "121"]
              ["314" "151"]]
             (-> req handler :body json/read-str))
          (-> req handler :body)))))
