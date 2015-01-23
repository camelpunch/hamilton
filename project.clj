(defproject hamilton "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [bidi "1.12.0"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [com.stuartsierra/component "0.2.2"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.8"]
                                  [org.clojure/tools.nrepl "0.2.7"]]
                   :plugins [[cider/cider-nrepl "0.8.1"]]}})