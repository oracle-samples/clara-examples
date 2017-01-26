(ns clara.examples.fact-type-options
  "Examples demonstrating usage of the :fact-type-fn and :ancestors-fn options for Clara session creation."
  (:require [clara.rules :refer :all]))

(defrule too-cold
  [:temperature-reading (< (:temperature this) 0)]
  =>
  (insert! {:weather-fact-type :weather-status
            :good-weather false}))

(defquery weather-status
  "Query for weather status"
  []
  [:weather-status (= ?good-weather (:good-weather this))])

(def custom-fact-type-fn :weather-fact-type)

(def custom-ancestors-fn {:precise-temperature-reading #{:temperature-reading}})

(def empty-session (mk-session [too-cold weather-status]
                               :fact-type-fn custom-fact-type-fn
                               :ancestors-fn custom-ancestors-fn))

(defn run-examples
  []
  (println "Query result with a fact of type :temperature-reading : "
           (-> empty-session
               (insert {:weather-fact-type :temperature-reading
                        :temperature -10})
               fire-rules
               (query weather-status)))

  (println "Query result with a custom ancestors function and a fact with a type that descends from :temperature-reading : "
           (-> empty-session
               (insert {:weather-fact-type :precise-temperature-reading
                        :temperature -10})
               fire-rules
               (query weather-status))))
