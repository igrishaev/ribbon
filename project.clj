(defproject ribbon "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [amazonica "0.3.101"]
                 [org.clojure/data.json "0.2.6"]
                 [com.cognitect/transit-clj "0.8.300"]]
  :main ^:skip-aot ribbon.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
