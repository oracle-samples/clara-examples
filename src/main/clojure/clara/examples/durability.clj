(ns clara.examples.durability

  (:require [clara.rules.accumulators :as acc]
            [clara.rules :refer :all]
            [clara.rules.durability :as d]))


;;;; Facts used in the examples below.

(defrecord Order [year month day])

(defrecord Customer [status])

(defrecord Purchase [cost item])

(defrecord Discount [reason percent])

(defrecord Total [total])

;;;; Some example rules. ;;;;

(defrule total-purchases
  (?total <- (acc/sum :cost) :from [Purchase])
  =>
  (insert! (->Total ?total)))

;;; Discounts.
(defrule summer-special
  "Place an order in the summer and get 20% off!"
  [Order (#{:june :july :august} month)]
  =>
  (insert! (->Discount :summer-special 20)))

(defrule vip-discount
  "VIPs get a discount on purchases over $100. Cannot be combined with any other discount."
  [Customer (= status :vip)]
  [Total (> total 100)]
  =>
  (insert! (->Discount :vip 10)))

(def max-discount
  "Accumulator that returns the highest percentage discount."
  (acc/max :percent :returns-fact true))

(defquery get-best-discount
  "Query to find the best discount that can be applied"
  []
  [?discount <- max-discount :from [Discount]])

;;;; Run the above example. ;;;;

;; Simulate external storage with an atom that may contain a serialized session.
(def stored-session (atom nil))

(defn save-session-state!
  "Save our session state to an external store."
  [session-state]
  (reset! stored-session (pr-str session-state)))

(defn read-session-state []
  "Read our session state from an external store."

  ;; Normally we'd use something like Fressian here...
  (read-string @stored-session))

;; Create an empty session with the above rules as our starting point.
;; This pattern of storing an empty session in a var allows for
;; efficient reuse and ensures it ha the same rule structure for all users.
(def empty-session (mk-session))

(defn run-examples
  "Function to run the above example."
  []
  (let [session (-> empty-session
                    (insert (->Customer :vip)
                            (->Order 2013 :march 20)
                            (->Purchase 20 :gizmo)
                            (->Purchase 120 :widget))
                    (fire-rules))]

    (println "Initial session query: " (query session get-best-discount))

    ;; Now save the session state to our mocked up external store.
    (save-session-state! (d/session-state session)))

  ;; Now re-create the session from storage and run the query again.
  (let [reloaded-state (read-session-state) ; Read the state from our mock external store.

        ;; Create a new session with the previous state.
        restored-session (d/restore-session-state
                          empty-session
                          reloaded-state)]

    (println "Restored session query:" (query restored-session get-best-discount))))
