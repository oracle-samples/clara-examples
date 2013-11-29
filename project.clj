(defproject org.toomuchcode/clara-examples "0.2.0-SNAPSHOT"
  :description "Clara Example Rules"
  :url "https://github.com/rbrush/clara-examples"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.google.guava/guava "15.0"] ; Explicitly pull new Guava version for dependency conflicts.
                 [org.clojure/clojure "1.5.1"]
                 [org.toomuchcode/clara-rules "0.3.0-SNAPSHOT"]
                 [org.toomuchcode/clara-storm "0.1.0-SNAPSHOT"]

                 ;; Dependencies for ClojureScript example.
                 [prismatic/dommy "0.1.1"]
                 [org.clojure/clojurescript "0.0-2030"]

                 ;; Dependency for time-based rules example.
                 [clj-time "0.6.0"]]

  :plugins [[lein-cljsbuild "1.0.0"]]
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :java-source-paths ["src/main/java"]
  :main clara.examples
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:source-paths ["src/main/clojurescript"]
                        :jar true
                        :compiler {:output-to "resources/public/js/examples.js"
                                   :optimizations :advanced}}]}

  ;; Austin for the ClojureScript REPL.
  :profiles {:dev {:plugins [[com.cemerick/austin "0.1.3"]]}}

  :scm {:name "git"
        :url "https://github.com/rbrush/clara-examples.git"}
  :pom-addition [:developers [:developer {:id "rbrush"}
                              [:name "Ryan Brush"]
                              [:url "http://www.toomuchcode.org"]]]
  :repositories [["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"}]]
  :deploy-repositories [["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                      :creds :gpg}]])
