(ns clj-blog.main
  (:require [clojure.java.io :as io]
            [clojure.tools.namespace.repl :as ns-tools])
  (:gen-class))

(ns-tools/disable-reload!)

(let [reload-dirs (->> (or (try
                             (require 'boot.core)
                             ((resolve 'boot.core/get-env) :directories)
                             (catch Exception _
                               nil))
                           (do (require 'clojure.tools.namespace.dir)
                               ((resolve 'clojure.tools.namespace.dir/dirs-on-classpath))))
                       (remove #(.exists (io/file % ".no-reload"))))]
  (apply ns-tools/set-refresh-dirs reload-dirs))

(defonce opts (atom {}))
(defonce system (atom nil))

(defn init []
  (require 'clj-blog.server)
  (swap! system (constantly {})))

(defn start []
  (swap! system (resolve 'clj-blog.server/start) @opts))

(defn stop []
  (swap! system (resolve 'clj-blog.server/stop)))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (ns-tools/refresh :after 'clj-blog.main/go))

(defn -main [& args]
  (require 'clj-blog.server)
  ((resolve 'clj-blog.server/start) {}))


