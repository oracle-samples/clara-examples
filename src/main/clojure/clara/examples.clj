(ns clara.examples
  (require [clara.examples.shopping :as shopping]
           [clara.examples.validation :as validation]
           [clara.examples.sensors :as sensors]
           [clara.examples.durability :as durability]
           [clara.examples.java.shopping :as jshopping]
           [clara.rules :as r]))

(defn -main []
  (println "Shopping examples:")
  (shopping/run-examples)
  (println)
  (println "JavaBean Shopping examples from Clojure:")
  (jshopping/run-examples)
  (println)
  (println "JavaBean Shopping examples from Java:")
  (clara.examples.java.ExampleMain/main (into-array String []))
  (println)
  (println "Validation examples:")
  (validation/run-examples)
  (println)
  (println "Sensor examples:")
  (sensors/run-examples)
  (println "Durability examples:")
  (durability/run-examples))
