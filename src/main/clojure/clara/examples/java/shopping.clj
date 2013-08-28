(ns clara.examples.java.shopping
  (:refer-clojure :exclude [==])
  (:require [clara.rules.accumulators :as acc]
            [clara.rules :refer :all])
  (:import [clara.examples.java Customer Order Promotion]))

(defrule awesome-promotion-for-vips
   "Creates an awesome promotion for VIPs that spend more than $200"
   [Customer (= true VIP)]
   [Order (> total 200)]
   =>
   (insert! (Promotion. "Awesome promotion for our favorite VIP!")))

(defquery get-promotions
   "Query the promotions."
   []
   [?promotion <- Promotion])
