(defproject org.toomuchcode/clara-examples "0.1.0-SNAPSHOT"
  :description "Clara Example Rules"
  :url "https://github.com/rbrush/clara-examples"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.toomuchcode/clara-rules "0.1.0"]
                 [clara/clara-storm "0.1.0-SNAPSHOT"]
                 [clj-time "0.5.1"]]
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :java-source-paths ["src/main/java"]
  :main clara.examples
  :aot :all)
