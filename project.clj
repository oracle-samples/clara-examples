(defproject org.toomuchcode/clara-examples "0.1.1"
  :description "Clara Example Rules"
  :url "https://github.com/rbrush/clara-examples"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.toomuchcode/clara-rules "0.3.0-SNAPSHOT"]
                 [org.toomuchcode/clara-storm "0.1.0-SNAPSHOT"]
                 [clj-time "0.5.1"]]
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :java-source-paths ["src/main/java"]
  :main clara.examples
  :scm {:name "git"
        :url "https://github.com/rbrush/clara-examples.git"}
  :pom-addition [:developers [:developer {:id "rbrush"}
                              [:name "Ryan Brush"]
                              [:url "http://www.toomuchcode.org"]]]
  :repositories [["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"}]]
  :deploy-repositories [["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                      :creds :gpg}]])
