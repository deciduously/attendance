(ns attendance.config)

(def system-env #(or (System/getenv %) %2))

(def config {:port (Integer/valueOf (system-env "PORT" "3000"))
             :version (-> "version.properties"
                          slurp
                          (clojure.string/split #"=")
                          second)})
