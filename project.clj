(defproject hamilton "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["array systems" {:url "http://corp.array.ca/nest-web/maven/"
                                   :checksum :ignore}]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [bidi "1.12.0"]
                 [liberator "0.12.2"]
                 [org.clojars.camelpunch/ordinance "0.1.0"]
                 [org.clojure/data.zip "0.1.1"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [uk.me.jstott/jcoord "1.0"]
                 [com.stuartsierra/component "0.2.2"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.8"]
                                  [org.clojure/tools.nrepl "0.2.7"]
                                  [ring/ring-mock "0.2.0"]]
                   :plugins [[cider/cider-nrepl "0.8.2"]]}})
