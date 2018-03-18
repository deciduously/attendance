(ns attendance.state
  (:require [attendance.parse :refer [roster extra]]
            [reagent.core :refer [atom]]
            [clojure.string :as string]
            [ajax.core :refer [GET]]))

;; RAtom - maybe pass in somewhere else

(defonce app-state (atom {:roster []}))

;; Utility

(defn update-state!
  "access util for 'thing' in app-state ratom"
  [thing f & args]
  (apply swap! app-state update-in thing f args))

;; Event dispatch

(defn blank-roster!
  "Clear roster data"
  []
  (update-state! [:roster] #(identity {})))

(defn read-roster!
  "Read in roster data"
  [roster-string]
  (update-state! [:roster] #(identity (roster roster-string))))

(defn refresh-roster!
  "Reset app state with fresh data"
  [roster-string]
  (blank-roster!)
  (read-roster! roster-string))

(defn refresh-extra!
  "Read extra hours CSV, swap into app-state"
  [extra-string]
  (update-state! [:roster :extra] #(identity (extra extra-string))))


;; TODO I think Specter has a better way to write these
;; But you wasted a ridiculous amount of time messing with it
;; Also, these are very similar.  Abstract!
(defn toggle-kid!
  "Toggle with idx"
  [idx]
  (update-state! [:roster :kids]
                 #(map (fn [k]
                         (if (= (:idx k) idx)
                           (update-in k [:here] not)
                           (identity k)))
                       %)))

(defn toggle-collected!
  "Toggle room collected status"
  [letter]
  (update-state! [:roster :core]
                 #(map (fn [r]
                         (if (= (:letter r) letter)
                           (update-in r [:collected] not)
                           (identity r)))
                       %)))

;; Non-destructive access

(defn get-kid
  "Pull a kid record from an idx"
  [idx]
  (->> (get-in @app-state [:roster :kids])
       (filter #(= idx (:idx %)))
       (first)))

(defn get-kid-room
  "Return the core room assignment and name of the given idx"
  [idx]
  {:room (->> (get-in @app-state [:roster :core])
              (filter #(some #{idx} (:enrolled %)))
              (first)
              (:letter))
   :name (-> idx
             get-kid
             :name)
   :idx idx})

(defn get-extended-room
  "Given a room letter, return the proper extended day room"
  [letter]
  (cond
    (some #{letter} ["A" "C"]) "CE"
    (some #{letter} ["B" "D"]) "DE"
    (some #{letter} ["E" "F" "G"]) "EE"
    (some #{letter} ["I" "J" "K"]) "JE"
    (some #{letter} ["H" "L" "M" "N" "O"]) "ME"
    :else (js/alert "Call Ben, get-extended-room is doing something silly")))

(defn get-kid-extended
  "Swap out room in get-kid-room for the proper extended assignment"
  [kid-map]
  (update-in kid-map [:room] get-extended-room))

(defn get-room
  "Pull the given room"
  [r]
  (->> (conj
        (get-in @app-state [:roster :core])
        (get-in @app-state [:roster :extended]))
       (flatten)
       (filter #(= r (:letter %)))
       (first)))

(defn absent
  "Get a list of the kids marked out in given room"
  [r]
  (->> (get-room r)
       (:enrolled)
       (map get-kid)
       (filter #(not (:here %)))))

(defn get-uncollected
  "Returns a list of which core rooms have not been marked Ready"
  []
  (->> (get-in @app-state [:roster :core])
       (filter #(not (:collected %)))
       (map :letter)))

;; File IO

(defn handle-file
  "OnChange listener for data file input"
  [id]
  (let [reader (js/FileReader.)
        file (-> (.getElementById js/document id)
                 .-files
                 (aget 0))]
    (set! (.-onload reader) (if (= id "data")
                              #(refresh-roster! (.-result reader))
                              #(refresh-extra! (.-result reader))))
    (.readAsText reader file)))

(defn mock-handler [res]
  (refresh-roster! (str res)))

(defn mock-extra-handler [res]
  (refresh-extra! (str res)))

(defn mock-error-handler [{:keys [status status-text]}]
  (.log (js/console (str "OH NO: " status " " status-text))))

(defn mock-data!
  "Grab the mock data and load it into the app-state"
  []
  (GET "/mock/roster/" {:handler mock-handler
                        :error-handler mock-error-handler})
  (GET "/mock/extra/" {:handler mock-extra-handler
                       :error-handler mock-error-handler}))

