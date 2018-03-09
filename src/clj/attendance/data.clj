(ns attendance.data
  (:require [attendance.parse :refer [parse-csv]]
            [clojure.java.io :refer [resource]]
            [clojure.string :refer [split-lines]]))

(def parse-csv-resource
  #(->> %
       (resource)
       (slurp)
       (parse-csv)
       (into [])))

(def mock-roster (-> "public/data/mock_roster.csv"
                     parse-csv-resource))

(def mock-extra (-> "public/data/mock_extra.csv"
                    parse-csv-resource))
