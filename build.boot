(set-env!
 :source-paths #{"src/cljs"}
 :resource-paths #{"resource"}

 :dependencies '[[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [adzerk/boot-cljs "2.1.4" :scope "test"]
                 [pandeiro/boot-http "0.8.3" :scope "test"]
                 [adzerk/boot-reload "0.5.2" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [com.cemerick/piggieback "0.2.2" :scope "test"]
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [weasel "0.7.0" :scope "test"]
                 [com.rpl/specter "1.1.0"]
                 [reagent "0.8.0-alpha2"]])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl-env start-repl]])

(deftask dev
  "Hot reloading dev environment with bREPL"
  []
  (comp
   (serve :dir "target")
   (watch)
   (reload)
   (cljs-repl-env)
   (cljs :optimizations :none)
   (target :dir #{"target"})))

(deftask build
  "Optimized bundle"
  []
  (comp
   (speak)
   (cljs :optimizations :advanced)
   (target :dir #{"target"})))
