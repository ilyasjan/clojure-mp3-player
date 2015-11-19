(defproject online-player "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojure "1.5.1"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [org.clojure/clojurescript "0.0-2202"]
                 ]
  :plugins [[lein-ring "0.8.13"]
            [lein-cljsbuild "1.0.3"]]
  :ring {:handler online-player.handler/app}
  :main ^:skip-aot online-player.handler
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]
                   }
             }
  :cljsbuild{:builds[{:source-paths ["cljs"]
                      :compiler {:output-to "resources/public/app.js"}}]}
  )
