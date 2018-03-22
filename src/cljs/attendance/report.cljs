(ns attendance.report
  (:require [attendance.state :as s]
            [clojure.string :as string]))

(defn firstlast-kid
  "Get first name, last initial for idx"
  [idx]
  (let [{:keys [name]} (s/get-kid idx)
        [firstname lastname] (string/split name #" ")]
    (str firstname " " (first lastname) ".")))

(defn report-kidlist
  "Report-friendly string from list of kids"
  [kids]
  (->> (map (comp firstlast-kid :idx) kids)
       (interpose ", ")
       (apply str)))

(defn report-room
  "Grab absent kids in room, formatted for core"
  [{:keys [letter enrolled]} room]
  (let [outs (s/absent letter)]
    (str
     (str "Room " letter ": ")
     (if (= 0 (count outs))
       "All here"
       (report-kidlist outs))
     "\r\n")))

(defn report-extended
  "Grab absent and added kids in room, formatted for extended"
  [{:keys [letter enrolled]} room]
  (let [outs (s/absent letter)
        added (->> (get-in @s/app-state [:roster :extra])
                   (map (comp s/get-kid-extended s/get-kid-room :idx))
                   (filter #(= letter (:room %))))
        num-out (count outs)
        num-added (count added)]
    (str
     (str (first letter) ": " (+ (- (count enrolled) num-out) num-added) " ")
     (if (= 0 num-out)
       ""
       (str "[No: " (report-kidlist outs)))
     (if (= 0 num-added)
       ""
       (str (if (> num-out 0) "; " "[") "Add: " (report-kidlist added)))
     (if (or (> num-out 0) (> num-added 0)) "]" "") "\r\n")))

;; TODO More similar functions...
(defn report
  "Make a readable report from an app-state"
  [state]
  (->> state
       (:core)
       (map report-room)
       (apply str)))

(defn email
  "Make a formatted extended day email from an app-state"
  [state]
  (let [date (js/Date.)]
    (str "Hi Everyone,\r\n\r\nHere are your extended day numbers for "
         (-> date (.toLocaleDateString js/Date))
         ":\r\n\r\n"
         (->> state
              (:extended)
              (map report-extended)
              (apply str))
         "\r\nThanks,\r\n")))

(defn aggregate-report
  "Concat reports"
  []
  (let [state (get-in @s/app-state [:roster])
        outs (report state)
        exts (email state)
        uncollected (s/get-uncollected)]
    (str
     (if (> (count uncollected) 0)
       (str "Double check the following rooms:\r\n"
            (->> uncollected
                 (interpose " ")
                 (apply str))
            "\r\n"))
     outs "\r\n" exts)))
