(ns clj-blog.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [immutant.web :as web]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer [site-defaults
                                              wrap-defaults]]
            [environ.core :refer [env]]))

(defroutes app-routes
  (route/not-found "Not Found"))

(def all-routes
  (routes app-routes))

(def http-handler
  (if is-dev?
    (reload/wrap-reload (wrap-defaults #'all-routes site-defaults))
    (wrap-defaults all-routes site-defaults)))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (print "Starting web server on port" port ".\n")
    (web/run http-handler {:port port})))

(defn run-auto-reload [& [port]]
  (auto-reload *ns*))

(defn run [& [port]]
  (when is-dev?
    (run-auto-reload)))
