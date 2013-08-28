(ns clara.examples
  (require [clara.examples.shopping :as shopping]
           [clara.examples.validation :as validation]
           [clara.examples.sensors :as sensors]))

(defn -main []
  (println "Shopping examples:")
  (shopping/run-examples)
  (println)
  (println "Validation examples:")
  (validation/run-examples)
  (println)
  (println "Sensor examples:")
  (sensors/run-examples))
