(set-env!
 :source-paths #{"src/cljs"}
 :resource-paths #{"static"}

 :dependencies '[;; Language
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]

                 ;; Util
                 [com.rpl/specter "1.1.0"]

                 ;; Frontend
                 [cljs-ajax "0.7.3"]
                 [reagent "0.8.0-alpha2"]
                ; [re-frame "0.10.5"] ; Make this happen, this is a good opportunity

                 ;; Dev tooling
                 [adzerk/boot-cljs "2.1.4" :scope "test"]
                 [adzerk/boot-reload "0.5.2" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [cider/cider-nrepl "0.17.0-SNAPSHOT" :scope "test"]
                 [com.cemerick/piggieback "0.2.2" :scope "test"]
                 [org.clojure/tools.nrepl "0.2.13" :scope "test"]
                 [weasel "0.7.0" :scope "test"]])

(task-options!
 pom {:project 'attendance
      :description "Attendance solver"})

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

(def system-env #(or (System/getenv %) %2))

(def config {:port (Integer/valueOf (system-env "PORT" "3000"))
             :version (-> "version.properties"
                          slurp
                          (clojure.string/split #"=")
                          second)})

(deftask run
  "Hot reloading dev environment with bREPL"
  []
  (comp
   ;(with-pass-thru _ ; use boot-http I think
   ;  (attendance.core/dev-main)
   (watch)
   (reload)
   (cljs-repl)
   (cljs :source-map true :optimizations :none :compiler-options {:asset-path "js/main.out"})
   (target :dir #{"target"})))

(deftask build
  "Optimized bundle"
  []
  (comp
   (cljs :optimizations :advanced)
   (target :dir #{"frontend"})))
