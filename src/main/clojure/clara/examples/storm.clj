(ns clara.examples.storm
  (:refer-clojure :exclude [==])
  (:use [backtype.storm bootstrap testing])
  (:use [backtype.storm.daemon common])
  (:use [backtype.storm clojure config])
  (:require [clara.rules.accumulators :as acc]
            [clara.rules :refer :all]
            [clara.rules.storm :refer :all]
            [clara.examples.sensors :as sensors])
  (:import [backtype.storm LocalDRPC LocalCluster StormSubmitter]
           [backtype.storm.topology TopologyBuilder]))

(defspout location-device-spout {FACT-STREAM ["fact"]}
  [conf context collector]
  (let [sent (atom false)]
    (spout
     (nextTuple []
                 
      ;; Only send location information once for this test.
      (when (not @sent)         
        (emit-spout! collector [[(sensors/->Location :room-1 :sector-5)]] :stream FACT-STREAM)
        (emit-spout! collector [[(sensors/->Location :room-2 :sector-5 )]] :stream FACT-STREAM)
        (emit-spout! collector [[(sensors/->Device 123 :room-1)]] :stream FACT-STREAM)
        (emit-spout! collector [[(sensors/->Device 456 :room-1)]] :stream FACT-STREAM)
        (emit-spout! collector [[(sensors/->Device 786 :room-2)]] :stream FACT-STREAM)
        (reset! sent true))

       (Thread/sleep 100))

     (ack [id]))))

(defspout temperature-spout {FACT-STREAM ["fact"]}
  [conf context collector]
  (let []
    (spout
     (nextTuple []

       ;; Emit a random temperature between 50 ad 150 degrees with the current time.
       (emit-spout! collector
                    [[(sensors/->TemperatureReading (+ 50 (rand-int 100)) 
                                                    (System/currentTimeMillis) 
                                                    :room-1)]]
                    :stream FACT-STREAM)
       (Thread/sleep 100))

     (ack [id]))))

(defn example-topology
  "Create a local topology for our example."
  []
  (let [builder (TopologyBuilder.)]

    (.setSpout builder "temperatures" temperature-spout nil)
    (.setSpout builder "locations" location-device-spout nil)
    (attach-topology builder
                     {:fact-source-ids ["temperatures" "locations"]
                      :rulesets ['clara.examples.sensors]})
    (.createTopology builder)))

(defn run-local-topology
  "Runs a local topology with generated facts against the sensor rules."
  []
  (let [cluster (LocalCluster.)
        topology (example-topology)]

    (.submitTopology cluster "test" {} topology)

    ;; Let some events process.
    (Thread/sleep 10000)    
    
    (.shutdown cluster)))

(defn -main []
  (run-local-topology))