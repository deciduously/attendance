(ns attendance.core
  (:require [attendance.ui :refer [ui]]
            [reagent.core :refer [atom render-component]]))

(defn init [] (render-component [ui] (.querySelector js/document "#app")))

(init)
