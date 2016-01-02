(ns clara.examples.sensors
  (:require [clara.rules.accumulators :as acc]
            [clara.rules :refer :all]))

;; A point-in-time temperature reading.
(defrecord TemperatureReading [value timestamp location])

;; The current temperature used by alerting and speed control logic.
(defrecord CurrentTemperature [value location])

;; A location used by sensors and devices.
(defrecord Location [id sector])

;; A device defined by an identifier and location.
(defrecord Device [id location])

;; The high threshold for temperatures.
(def high-threshold 120)

;; The low threshold for temperatures.
(def low-threshold 80)

;;; Function placeholders to simulate taking actions based on sensor readings. ;;;

(defn alert-temperature!
  "Fires a temperature alert. A real system probably wouldn't use a println here."
  [value location sector]
  (println "ALERT: temperature of" value "at" location "in sector" sector))

(defn reduce-speed!
  "Instruct the device to reduce speed."
  [device]
  (println "Reducing speed of " device))

(defn increase-speed!
  "Instructs the device to increase speed."
  [device]
  (println "Increasing speed of " device))

;;; The example rules themselves ;;;

;; An accumulator that produces the "newest" temperature by returning the Temperature fact with the maximum timestamp.
;; The acc/max and related functions can also be called inline in the rule; here we define it independently for readability.
(def newest-temp (acc/max :timestamp :returns-fact true))

;; Simple example of getting current temperature. This rule could be replaced by
;; more sophisticated logic, like the mean of the last five temperatures to discard outliers.
(defrule get-current-temperature
  "Get the current temperature at a location by simply looking at the newest reading."
  [?current-temp <- newest-temp :from [TemperatureReading (= ?location location)]]
  =>
  (insert! (->CurrentTemperature (:value ?current-temp) ?location)))

(defrule temperature-alert
  "Issue a temperature alert. This rule joins the current temperature with the location
   and gathers additional information to fire an alert with context."
  [CurrentTemperature (> value high-threshold)
                      (= ?location-id location)
                      (= ?value value)]

  [Location (= ?location-id id)
            (= ?sector sector)]
  =>
  (alert-temperature! ?value ?location-id ?sector))


(defrule reduce-device-speed
  "Reduce the speed of all devices in a location that has a high temperature."
  [CurrentTemperature (> value high-threshold)
                      (= ?location-id location)]

  ;; Find all Device records in the location, and bind them to the ?device variable.
  [?device <- Device (= ?location-id location)]
  =>
  (reduce-speed! ?device))

(defrule increase-device-speed
  "Increase the speed of all devices in a location that has a low temperature."
  [CurrentTemperature (< value low-threshold)
                      (= ?location-id location)]

  ;; Find all Device records in the location, and bind them to the ?device variable.
  [?device <- Device (= ?location-id location)]
  =>
  (increase-speed! ?device))

(defn run-examples
  "Run the examples."
  []
  ;; Create a session with our location and device information.
  (let [session
        (-> (mk-session 'clara.examples.sensors)
            (insert (->Location :room-1 :sector-5 )
                    (->Location :room-2 :sector-5 )
                    (->Device 123 :room-1)
                    (->Device 456 :room-1)
                    (->Device 786 :room-2)))]

    (-> session

        ;; Insert some temperature readings, the latest of which triggers an alert for room 1.
        (insert (->TemperatureReading 100 1 :room-1))
        (insert (->TemperatureReading 110 2 :room-1))
        (insert (->TemperatureReading 130 3 :room-1))
        (fire-rules) ;; Prints an alert and reduce speed messages for devices in room 1

        ;; Simulate reducing temperatures, allowing our devices to increase speed again.
        (insert (->TemperatureReading 100 4 :room-1))
        (insert (->TemperatureReading 85 5 :room-1))
        (insert (->TemperatureReading 75 6 :room-1))
        (fire-rules))) ;; Prints increase speed messages for devices in room 1.

  nil)
