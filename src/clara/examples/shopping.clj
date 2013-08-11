(ns clara.examples.shopping
  (:refer-clojure :exclude [==])
  (:require [clara.rules.accumulators :as acc]
            [clara.rules :refer :all]))

;;;; Facts used in the examples below.

(defrecord Order [year month day])

(defrecord Customer [status])

(defrecord Purchase [cost])

(defrecord Discount [reason percent])

(defrecord Total [total])

(defrecord Coupon [percent])

;;;; Some example rules. ;;;;

(defrule total-purchases
  (?total <- (acc/sum :cost) :from [Purchase])
  =>
  (insert! (->Total ?total)))

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


;;;; The section below shows this example in action. ;;;;

(defn print-discounts! 
  "Print the discounts from the given session."
  [session]

  ;; Destructure and print each discount.
  (doseq [{{reason :reason percent :percent} :?discount} (query session get-best-discount {})] 
    (println percent "%" reason "discount"))

  session)

(defn run-examples 
  "Function to run the above example."
  []
  (println "VIP shopping example:")
  ;; prints "10 % :vip discount"
  (-> (mk-session 'clara.examples.shopping) ; Load the rules.
      (insert (->Customer :vip)) ; Insert some facts.
      (insert (->Order 2013 :march 20))
      (insert (->Purchase 20))
      (insert (->Purchase 120))
      (fire-rules)
      (print-discounts!))

  (println "Summer special example:")
  ;; prints "20 % :summer-special discount"
  (-> (mk-session 'clara.examples.shopping) ; Load the rules.
      (insert (->Customer :vip)) ; Insert some facts.
      (insert (->Order 2013 :july 20))
      (insert (->Purchase 20))
      (insert (->Purchase 120))
      (fire-rules)
      (print-discounts!))

  nil)
