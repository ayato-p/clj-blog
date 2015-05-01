(ns clj-blog.routes.home
  (:require [compojure.core :refer :all]
            [net.cgrand.enlive-html :as html]))

(html/deftemplate home-template "templates/layouts/application.html"
  [])

(defroutes home-routes
  (GET "/" [] (home-template)))
