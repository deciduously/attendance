(ns attendance.data
  (:require [attendance.parse]
            [clojure.java.io :refer [resource]]
            [clojure.string :refer [split-lines]]))

(def parse-csv-resource
  #(-> %
       resource
       slurp))

(def mock-roster (parse-csv-resource "public/data/mock_roster.csv"))

(def mock-extra (parse-csv-resource "public/data/mock_extra.csv"))
