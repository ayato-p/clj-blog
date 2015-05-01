(ns clj-blog.server
  (:require [clj-blog.routes.home :refer [home-routes]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [immutant.web :as web]
            [ring.middleware.defaults :refer [site-defaults
                                              wrap-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.http-response :refer [ok content-type]]))

(defroutes base-routes
  (route/resources "/js" {:root "js"})
  (route/resources "/css" {:root "css"}))

(def all-routes
  (routes
   home-routes
   base-routes))

(defn stop
  [{:keys [immutant-server] :as ctx}]
  (when immutant-server
    (immutant-server))
  {})

(defn start
  [ctx & [{:keys [port reload reload-dirs]}]]
  (let [handler (cond-> #'clj-blog.server/all-routes
                  reload (wrap-reload {:dirs (seq reload-dirs)}))
        port (Integer. (or port 10555))
        immutant-server (web/run handler {:port port})]
    (println "Starting web server on port" port)
    {:immutant-server immutant-server}))
