(ns clara.examples.tools
  "Examples using Clara tools."
  (:require [clara.rules :refer :all]
            [clara.tools.ui :as ui]
            [clara.examples.shopping :refer :all]))

(defn run-ui-examples
  "Run examples that shows the clara-tools ui."
  []

  (let [session (-> (mk-session 'clara.examples.shopping :cache false) ; Load the rules.
                    (insert (->Customer :vip)
                            (->Order 2013 :august 20)
                            (->Purchase 20 :gizmo)
                            (->Purchase 120 :widget)
                            (->Purchase 90 :widget)) ; Insert some facts.
                    (fire-rules))]
    (print-discounts! session)
    (print-promotions! session)

    (println "Showing session in a browser...")
    (ui/show-session session)

    (println "Showing logical structure in a browser...")
    (ui/show-logic-graph ['clara.examples.shopping]))
    (println "Press enter to continue...")
    (read-line))

(defn -main []
  (run-ui-examples)
  ;; Shut down the server so the JVM will exit.
  (clara.tools.ui.server/stop-server!)
  (shutdown-agents))
