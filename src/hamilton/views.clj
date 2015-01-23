(ns hamilton.views
  (:require [hiccup.page :refer [html5]]))

(defn homepage [_]
  (html5
   [:body [:h1 "Hamilton"]]))
