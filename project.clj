(defproject clara/clara-examples "0.1.0-SNAPSHOT"
  :description "Clara Example Rules"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clara/clara-rules "0.1.0-SNAPSHOT"]
                 [clara/clara-storm "0.1.0-SNAPSHOT"]
                 [clj-time "0.5.1"]]
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :java-source-paths ["src/main/java"]
  :main clara.examples
  :aot :all)
