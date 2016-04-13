(ns clara.examples.booleans
  "Examples to demonstrate the use of boolean conditions that are compiled into the rules network."
  (:require [clara.rules :refer :all]))

(defrecord Order [customer-id order-id amount])

(defrecord CustomerAppreciationDay [day])

(defrecord ValuedCustomer [customer-id])

(defrecord NewCustomer [customer-id])

(defrecord CurrentDay [day])

(defrecord Discount [order-id amount])

(defrule new-or-valued-customer-basic-coupons
  [Order (= ?order-id order-id) (= ?customer-id customer-id)]
  [:or
   [NewCustomer (= ?customer-id customer-id)]
   [ValuedCustomer (= ?customer-id customer-id)]]
  =>
  (insert! (->Discount ?order-id 5)))

(defrule appreciation-day-and-valued-or-new
  [Order (= ?order-id order-id) (= ?customer-id customer-id)]
  [:or
   [:and
    [CurrentDay (= ?day day)]
    [CustomerAppreciationDay (= ?day day)]]
   [:and
    [NewCustomer (= ?customer-id customer-id)]
    [ValuedCustomer (= ?customer-id customer-id)]]]
  =>
  (insert! (->Discount ?order-id 20)))

(defrule insert-discount-for-specific-customer
  [Order
   ;; Deliberately make both branches of the or equivalent
   ;; to demonstrate that this is a short-circuiting or.
   (or (contains? #{3} customer-id)
       (= 3 customer-id))
   (= ?order-id order-id)]
  =>
  (insert! (->Discount ?order-id 10)))

(defquery discount-query
  "Query for discounts by amount and order ID"
  []
  [Discount (= ?order-id order-id) (= ?amount amount)])

(defn run-examples
  "Run the examples."
  []
  ;; The rule new-or-valued-customer-basic-coupons matches each Order against each fact in the session
  ;; in the session that is either a NewCustomer or ValuedCustomer fact with the same customer-id as that order.
  ;; Note that unlike clojure.core/or, this is *not* short-circuiting.
  ;; Therefore two discounts of 5 units are inserted.
  ;;
  ;; Furthermore, since the customer is both a ValuedCustomer and a NewCustomer, appreciation-day-and-valued-or-new
  ;; inserts an additional discount of 20 units.
  (let [empty-session (mk-session 'clara.examples.booleans)]
    (let [both-new-and-valued (-> empty-session
                                  (insert (->Order 1 1 100)
                                          (->NewCustomer 1)
                                          (->ValuedCustomer 1))
                                  fire-rules
                                  (query discount-query))]
      (println "Discounts when a customer is both a NewCustomer and a ValuedCustomer: " both-new-and-valued))

    ;; Related to the example above, new-or-valued-customer-basic-coupons matches every NewCustomer fact against
    ;; relevant Order facts; it does not stop at the first match for each type.
    ;; Therefore two discounts of 5 units are inserted.
    (let [doubly-new-customer (-> empty-session
                                  (insert (->Order 1 1 100)
                                          (->NewCustomer 1)
                                          (->NewCustomer 1))
                                  fire-rules
                                  (query discount-query))]
      (println "Discounts when the customer is noted as new twice in the session: " doubly-new-customer))

    ;; The session has a CustomerAppreciationDay and a CurrentDay fact, but the bindings between them do not match.
    ;; Therefore no discounts are inserted.
    (let [not-customer-appreciation-day (-> empty-session
                                            (insert (->Order 1 1 100)
                                                    (->CurrentDay "2016-01-01")
                                                    (->CustomerAppreciationDay "2015-12-31"))
                                            fire-rules
                                            (query discount-query))]
      (println "Discounts when there is a CustomerAppreciationDay, but it does not match the CurrentDay: " not-customer-appreciation-day))

    ;; In this case the "or" is inside a condition on a particular fact type, not compiled into the network.  This will have the
    ;; the short-circuiting behavior of clojure.core/or.  A single discount of 10 units will be inserted.
    (let [customer-3-order (-> empty-session
                               (insert (->Order 3 3 100))
                               fire-rules
                               (query discount-query))]
      (println "Discounts when an Order matches both branches of clojure.core/or : " customer-3-order))))
