(ns clara.examples.shopping
  "Shopping example adapted to ClojureScript, updating the DOM to display results. 
   Real examples would actually have styling."
  (:require-macros [clara.macros :refer [defrule defquery mk-session]]
                   [dommy.macros :refer [node sel sel1]])
  (:require [dommy.utils :as utils] ; dommy used to manipulate the DOM.
            [dommy.core :as dommy]
            [clara.rules.accumulators :as acc]
            [clara.rules :refer [insert retract fire-rules query insert! retract!]]))


;;;; Facts used in the examples below.

(defrecord Order [year month day])

(defrecord Customer [status])

(defrecord Purchase [cost item])

(defrecord Discount [reason percent])

(defrecord Total [total])

(defrecord Promotion [reason type])

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

;;; Promotions.
(defrule free-widget-month
  "All purchases over $200 in August get a free widget."
  [Order (= :august month)]
  [Total (> total 200)]
  =>
  (insert! (->Promotion :free-widget-month :widget)))

(defrule free-lunch-with-gizmo
  "Anyone who purchases a gizmo gets a free lunch."
  [Purchase (= item :gizmo)]
  =>
  (insert! (->Promotion :free-lunch-with-gizmo :lunch)))

(defquery get-promotions
  "Query to find promotions for the purchase."
  []
  [?promotion <- Promotion])

;;;; The section below shows this example in action. ;;;;

(defn show-discounts! 
  "Print the discounts from the given session."
  [session]

  ;; Destructure and print each discount.
  (doseq [{{reason :reason percent :percent} :?discount} (query session get-best-discount)]
    (dommy/append! (sel1 :#shopping) [:.discount (str percent "% " reason " discount")]) )

  session)

(defn show-promotions!
  "Prints promotions from the given session"
  [session]

  (doseq [{{reason :reason type :type} :?promotion} (query session get-promotions)] 
    (dommy/append! (sel1 :#shopping) [:.promotion  (str "Free " type " for promotion " reason)]) )
  
  session)

(defn run-examples 
  "Function to run the above example."
  []

  (dommy/append! (sel1 :#shopping) [:.overview "VIP Shopping Example:"])

  ;; prints "10 % :vip discount"
  (-> (mk-session 'clara.examples.shopping) ; Load the rules.
      (insert (->Customer :vip)
              (->Order 2013 :march 20)
              (->Purchase 20 :gizmo)
              (->Purchase 120 :widget)) ; Insert some facts.
      (fire-rules)
      (show-discounts!))

  (dommy/append! (sel1 :#shopping) [:.overview  "Summer special and widget promotion example:"])

  ;; prints: "20 % :summer-special discount"
  ;;         "Free :lunch for promotion :free-lunch-for-gizmo"
  ;;         "Free :widget for promotion :free-widget-month"
  (-> (mk-session 'clara.examples.shopping) ; Load the rules.
      (insert (->Customer :vip)
              (->Order 2013 :august 20)
              (->Purchase 20 :gizmo)
              (->Purchase 120 :widget)
              (->Purchase 90 :widget)) ; Insert some facts.
      (fire-rules)
      (show-discounts!)
      (show-promotions!))

  nil)