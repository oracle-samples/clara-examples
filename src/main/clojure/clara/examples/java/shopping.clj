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


(defn run-examples 
  "Function to run the above example. Also see ExampleMain.java to see this used from Java. "
  []
  (let [session (-> (mk-session 'clara.examples.java.shopping :cache false) ; Load the rules.
                    (insert (Customer. "Tim" true)
                            (Order. 250)) ; Insert some facts.
                    (fire-rules))]

    (doseq [{promotion :?promotion} (query session get-promotions)]
      (println "Promotion:" (.getDescription promotion))))

  nil)
