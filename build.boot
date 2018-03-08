(set-env!
 :source-paths #{"src/clj" "src/cljc" "src/cljs"}
 :resource-paths #{"resources"}

 :dependencies '[[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [adzerk/boot-cljs "2.1.4" :scope "test"]
                 ;[pandeiro/boot-http "0.8.3" :scope "test"]
                 [adzerk/boot-reload "0.5.2" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [com.cemerick/piggieback "0.2.2" :scope "test"]
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [weasel "0.7.0" :scope "test"]
                 [com.rpl/specter "1.1.0"]
                 [reagent "0.8.0-alpha2"]
                 [ring "1.6.3"]])

(task-options!
 pom {:project 'attendance
      :description "Attendance solver in Reagent"}
 aot {:namespace '#{attendance.core}}
 jar {:main 'attendance.core}
 sift {:include #{#"\.jar$"}})

(require '[adzerk.boot-cljs :refer [cljs]]
         ;'[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         'attendance.core)

(deftask run
  "Hot reloading dev environment with bREPL"
  []
  (comp
   (with-pass-thru _
     (attendance.core/dev-main))
   (watch)
   (reload :asset-path "public")
   (cljs-repl :nrepl-opts {:port 9009})
   (cljs :source-map true :optimizations :none :compiler-options {:asset-path "main.out"})
   (target :dir #{"target"})))

(deftask build
  "Optimized bundle"
  []
  (comp
   (cljs :optimizations :advanced)
   (aot)
   (pom :version (-> "version.properties"
                     slurp
                     (clojure.string/split #"=")
                     second))
   (uber)
   (jar)
   (sift)
   (target :dir #{"target"})))
