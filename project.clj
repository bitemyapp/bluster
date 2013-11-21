(defproject bluster "0.0.1"
  :description "SwaggerUI JSON generator for Clojure"
  :url "http://github.com/bitemyapp/bluster"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repl-options {:port 39125}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cheshire "5.2.0"]]
  :profiles {:dev {:dependencies [[clj-http "0.7.7"]]}})
