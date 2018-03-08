(ns attendance.core
  (:require [attendance.ui :refer [ui]]
            [reagent.core :refer [atom render-component]]))

(defn init []
  (let [state (atom {:roster {}})]
    (render-component [ui state] (.querySelector js/document "#app"))))

(init)
