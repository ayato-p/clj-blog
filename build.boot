(set-env! :source-paths #{"src/clj" "src/cljs" "src/less"}
          :resource-paths #{"resources"}
          :dependencies '[
                          ;; task
                          [adzerk/boot-cljs "0.0-2814-4" :scope "test"]
                          [adzerk/boot-reload "0.2.6" :scope "test"]
                          [danielsz/boot-environ "0.0.1"]
                          [deraen/boot-less "0.3.0" :scope "test"]

                          ;; backend
                          [compojure "1.3.3"]
                          [environ "1.0.0"]
                          [enlive "1.1.5"]
                          [metosin/ring-http-response "0.5.2"]
                          [org.clojure/clojure "1.7.0-beta2"]
                          [org.clojure/tools.namespace "0.2.10"]
                          [org.immutant/immutant "2.0.0"]
                          [ring/ring-defaults "0.1.4"]
                          [ring/ring-devel "1.3.2"]

                          ;; database
                          [org.postgresql/postgresql "9.4-1201-jdbc41"]
                          [yesql "0.4.0"]

                          ;; frontend
                          [org.clojure/clojurescript "0.0-3211"]
                          [quiescent "0.2.0-alpha1"]
                          ])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[deraen.boot-less :refer [less]]
         '[danielsz.boot-environ :refer [environ]]
         '[clj-blog.boot :refer :all])

(task-options!
 pom {:project 'clj-blog
      :version "0.1.0-SNAPSHOT"
      :description ""
      :license {"The MIT License (MIT)" "http://opensource.org/licenses/mit-license.php"}}
 aot {:namespace #{'clj-blog.main}}
 jar {:main 'clj-blog.main}
 cljs {:compiler-options
       {:output-to "app.js"
        :optimizations :none
        :pretty-print  true}}
 less {:source-map true})

;; (deftask wrap-less []
;;   (comp (less) (sift :move {#"main.css" "resources/public/css/app.css"})))

(deftask dev
  "Start the dev env ..."
  []
  (comp
   (environ :env {:dev true})
   (watch)
   (reload :on-jsload 'clj-blog.core/start!)
   (less)
   ;; (wrap-less)
   (cljs :optimizations :none
         :unified-mode true
         )
   (start-app :port 3000 :reload true)))

(deftask package
  "Build the package"
  []
  (comp
   (less :compression true)
   (cljs :optimizations :advanced)
   (aot)
   (pom)
   (uber)
   (jar)))
