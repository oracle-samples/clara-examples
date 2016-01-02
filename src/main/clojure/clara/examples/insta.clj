(ns clara.examples.insta
  "Instantly create a rule-based DSL"
  (:require [instaparse.core :as insta]
            [clara.rules.accumulators :as acc]
            [clara.rules :refer :all]
            [schema.core :as s]))

;;;; Facts used in the examples below.

(defrecord Order [year month day])

(defrecord Customer [status])

(defrecord Purchase [cost item])

(defrecord Discount [name percent])

(defrecord Total [value])

(defrecord Promotion [reason type])

(def shopping-grammar
  (insta/parser
   "<RULES> = [DISCOUNT | PROMOTION]+
    PROMOTION = <'promotion'> NAME PROMOTIONTYPE <'when'> CONDITION [<'and'> CONDITION]* <'.'>;
    DISCOUNT = <'discount'> NAME PERCENT <'when'> CONDITION [<'and'> CONDITION]* <'.'>;
    <PERCENT> = NUMBER ;
    PROMOTIONTYPE = STRING ;
    <NAME> = STRING ;
    NUMBER = #'[0-9]+' ;
    <STRING> = #'[A-Za-z][A-Za-z0-9_-]+' ;
    CONDITION = FACTTYPE FIELD OPERATOR VALUE ;
    FACTTYPE = 'customer' | 'total' | 'order' ;
    <FIELD> = STRING ;
    OPERATOR = 'is' | '>' | '<' | '=' ;
    <VALUE> = STRING | NUMBER ;
    "
   :auto-whitespace :standard))

(def operators {"is" `=
                ">" `>
                "<" `<
                "=" `=})

(def fact-types
  {"customer" Customer
   "total" Total
   "order" Order})

(def shopping-transforms
  {:NUMBER #(Integer/parseInt %)
   :OPERATOR operators
   :FACTTYPE fact-types
   :CONDITION (fn [fact-type field operator value]
                {:type fact-type
                 :constraints [(list operator (symbol field) value)]})

   ;; Convert promotion strings to keywords.
   :PROMOTIONTYPE keyword

   :DISCOUNT (fn [name percent & conditions]
               {:name name
                :lhs conditions

                :rhs `(insert! (->Discount ~name ~percent))})

   :PROMOTION (fn [name promotion-type & conditions]
                {:name name
                 :lhs conditions
                 :rhs `(insert! (->Promotion ~name ~promotion-type))})})


;; These rules may be stored in an external file or database.
(def example-rules

  "discount my-discount 15 when customer status is platinum.
   discount extra-discount 10 when customer status is gold and total value > 200.
   promotion free-widget-month free-widget when customer status is gold and order month is august.")


;;;; Rules written in Clojure and combined with externally-defined rules.

(defrule total-purchases
  (?total <- (acc/sum :cost) :from [Purchase])
  =>
  (insert! (->Total ?total)))

(defquery get-discounts
  "Returns the available discounts."
  []
  [?discount <- Discount])

(defquery get-promotions
  "Returns the available promotions."
  []
  [?discount <- Promotion])

;;;; Example code to load and validate rules.

(s/defn ^:always-validate load-user-rules :- [clara.rules.schema/Production]
  "Converts a business rule string into Clara productions."
  [business-rules :- s/Str]

  (let [parse-tree (shopping-grammar business-rules)]

    (when (insta/failure? parse-tree)
      (throw (ex-info (print-str parse-tree) {:failure parse-tree})))

    (insta/transform shopping-transforms parse-tree)))

(defn run-examples
  "Run the example."
  []
  (let [session (-> (mk-session 'clara.examples.insta (load-user-rules example-rules))
                    (insert (->Customer "gold")
                            (->Order 2013 "august" 20)
                            (->Purchase 20 :gizmo)
                            (->Purchase 120 :widget)
                            (->Purchase 90 :widget))
                    (fire-rules))]

    (clojure.pprint/pprint (query session get-discounts))
    (clojure.pprint/pprint (query session get-promotions))))
