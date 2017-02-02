(ns clara.examples.truth-maintenance
  (:require [clara.rules.accumulators :as acc]
            [clara.rules :refer :all]))

(defrecord Temperature [temperature location])

(defrecord LocalTemperatureRecords [high low location])

(defrecord Cold [temperature])

(defrecord AlwaysOverZeroLocation [location])

(defrule insert-temperature-records
  [?min-temp <- (acc/min :temperature) :from [Temperature (= ?loc location)]]
  [?max-temp <- (acc/max :temperature) :from [Temperature (= ?loc location)]]
  =>
  (insert! (map->LocalTemperatureRecords {:high ?max-temp :low ?min-temp :location ?loc})))

;; When a Temperature fact is inserted or retracted, the output of insert-temperature-records will
;; be adjusted to compensate, and the output of this rule will in turn be adjusted to compensate for the
;; change in the LocalTemperatureRecords facts in the session.
(defrule always-over-zero
  [LocalTemperatureRecords (> low 0) (= ?loc location)]
  =>
  (insert! (->AlwaysOverZeroLocation ?loc)))

(defrule insert-cold-temperature
  [Temperature (= ?temperature temperature) (< temperature 30)]
  =>
  (insert! (->Cold ?temperature)))

(defquery cold-facts
  "Query for Cold facts"
  []
  [Cold (= ?temperature temperature)])

(defquery records-facts
  "Query for LocalTemperatureRecord facts"
  []
  [LocalTemperatureRecords (= ?high high) (= ?low low) (= ?loc location)])

(defquery always-over-zero-facts
  "Query for AlwaysOverZeroLocation facts"
  []
  [AlwaysOverZeroLocation (= ?loc location)])

(defn run-examples []
  (let [initial-session (-> (mk-session 'clara.examples.truth-maintenance)
                            (insert (->Temperature -10 "MCI")
                                    (->Temperature 110 "MCI")
                                    (->Temperature 20 "LHR")
                                    (->Temperature 90 "LHR"))
                            fire-rules)]

    (println "Initial cold temperatures: "
             (query initial-session cold-facts))
    (println "Initial local temperature records: "
             (query initial-session records-facts))
    (println "Initial locations that have never been below 0: "
             (query initial-session always-over-zero-facts))
    (println "")
    (println "Now add a temperature of -5 to LHR and a temperature of 115 to MCI")

    (let [with-mods-session (-> initial-session
                                (insert (->Temperature -5 "LHR")
                                        (->Temperature 115 "MCI"))
                                fire-rules)]
      (println "New cold temperatures: "
               (query with-mods-session cold-facts))
      (println "New local temperature records: "
               (query with-mods-session records-facts))
      (println "New locations that have never been below 0: "
               (query with-mods-session always-over-zero-facts))

      (let [with-retracted-session (-> with-mods-session
                                       (retract (->Temperature -5 "LHR"))
                                       fire-rules)]

        (println "")
        (println "Now we retract the temperature of -5 at LHR")
        (println "Cold temperatures with this retraction: "
                 (query with-retracted-session cold-facts))
        (println "Local temperature records with this retraction: "
                 (query with-retracted-session records-facts))
        (println "Locations that have never been below zero with this retraction: "
                 (query with-retracted-session always-over-zero-facts))))))
