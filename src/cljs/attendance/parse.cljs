(ns attendance.parse
  (:require [clojure.string :as string]
            [com.rpl.specter :as sp]))

(def test-core #(= 1 (count (first %))))

(defn parse-csv
  "Parse CSV string data into Clojure data structure"
  [string]
  (->> (string/split-lines string)
       (map #(string/split % #","))))

(defn kid-record
  "Create a record from a name"
  [name]
  {:idx (hash name) :name name :here true})

(defn parse-room
  "Parse vector of string fields into room map"
  [fields]
  {:letter (first fields)
   :max (-> (second fields) js/parseInt)
   :enrolled (into [] (map hash (drop 2 fields)))
   :collected false})

;; TODO add notes even if they're in core
;; This only checks if the hour specified bumps into extended at all
(defn parse-extra-hours
  [hour-string]
  (> (-> hour-string
         (string/split #"-")
         second
         js/parseInt)
     4))

(defn parse-extra
  "Parse extra hours vector into add hours entry"
  [fields]
  (let [idx (-> fields first hash)]
    {:idx idx
     :extended (-> fields second parse-extra-hours)}))

(defn kids
  "Pull just the kids, swapping for respective maps"
  [raw]
  (sp/transform
   [sp/ALL]
   kid-record
   (->> raw
        (take-while test-core)
        (map #(drop 2 %))
        (flatten))))

(defn roster
  "Take a csv string to prepared roster data structure"
  [roster-string]
  (let [raw (parse-csv roster-string)]
    {:kids (into [] (kids raw))
     :core (into [] (map parse-room (take-while test-core raw)))
     :extended (into [] (map parse-room (drop-while test-core raw)))}))

(defn extra
  "Take a csv string to prepared extra hours data structure"
  [extra-string]
  (let [raw (parse-csv extra-string)]
    (into [] (->> raw
                  (map parse-extra)
                  (filter :extended))))) ;TODO keep core kids, indicate somehow
