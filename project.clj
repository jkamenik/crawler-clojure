(defproject crawler-clojure "0.1.0-SNAPSHOT"
  :description "A simple web crawler"
  :url "https://github.com/jkamenik/crawler-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [clj-http "1.1.0"]
                 [enlive "1.1.5"]]
  :main ^:skip-aot crawler-clojure.core)
