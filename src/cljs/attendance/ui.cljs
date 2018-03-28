(ns attendance.ui
  (:require [attendance.report :refer [aggregate-report]]
            [attendance.state :as s]
            [ajax.core :refer [POST]]))

;; Helpers

(defn button
  "Button utility"
  ([class title action]
   [:input {:type "button" :class class :value title :on-click action}]))

(defn file-upload
  "File upload utility"
  [id type]
  [:input {:type "file" :id id :accept type :onChange #(s/handle-file id)}])

(defn http-link
  "A href helper"
  ([url] [:a {:href url} url])
  ([url title] [:a {:href url} title]))

;; Components

(defn file-console
  "Buttons for managing the roster file"
  []
  [:form#file-console
   [button "console" "Load Mock Data" #(s/mock-data!)] [:br] [:br]
   "Roster: " [file-upload "data" ".csv"] [:br]
   "Extra Hours: " [file-upload "extra" ".csv"] [:br] [:br]
   [button "console" "Check Report" #(js/alert (aggregate-report))]
   " "
   [:a.export
    {:href (str "data:application/octet-stream;charset=utf8;base64,"
                (-> (aggregate-report) js/btoa)/)}
    [button "console" "Download Report" #()]]])

(defn kid
  "Render a kid - accepts a kid record and a boolean - true for core"
  [k core?]
  (let [{:keys [idx name here]} k
        status (if here "In" "Out")
        kname (str name " - " status)]
    [:li.kid
     (if core?
       [button status kname #(s/toggle-kid! idx)]
       [:span {:class status} kname])]))

(defn kidlist
  "Render a class roster from a list of kids"
  [max enrolled core?]
  (let [kids (map s/get-kid enrolled)]
    [:ul.kidlist
     [:li.capacity
      (str (count (filter :here kids)) "/" (count kids)
           " Max: " max)]
     (for [k kids]
       ^{:key (:idx k)}
       [kid k core?])]))

(defn absentlist
  "Render a list of the absent kids in the given room letter"
  [letter]
  [:ul.absentlist
   [:li [:h4 "Out"]]
   (doall
    (for [out (s/absent letter)]
      (let [n (:name out)]
        ^{:key n}
        [:li.absent n])))])

(defn room
  "Render a class of children"
  [{:keys [letter max enrolled collected]} r]
  (let [core? (= 1 (count letter))]
    [:li.room
     [:div.roomContent
      [:h4.roomletter (str "Room " letter) [:br]
       (if (= 1 (count letter))
        [button
         "console"
         (if collected "Ready" "Need to finish")
         #(s/toggle-collected! letter)])]
      [kidlist max enrolled core?]
      (if core? [absentlist letter])]]))

(defn roster
  "Render all rooms given"
  [roster]
  [:div.roster
   [:ul.classlist
    (for [class roster]
      ^{:key (:letter class)}
      [room class])]])

(defn footer
  "Render the footer"
  []
  [:footer
   (str ".:|- " \u00A9 " 2018 deciduously - ")
   (http-link "https://github.com/deciduously/attendance" "source")
   " -|:."])

;; Main UI

(defn ui
  "App parent component"
  []
  [:div
   [:h1 "Attendance"]
   [:hr]
   [file-console] [:hr]
   [roster (get-in @s/app-state [:roster :core])]
   [:hr]
   [roster (get-in @s/app-state [:roster :extended])]
   [footer]])
