(set-env! :source-paths #{"src/clj" "src/cljs" "src/less"}
          :resource-paths #{"resources/public" "resources/templates"}
          :dependencies '[
                          ;; task
                          [adzerk/boot-cljs "0.0-2814-4" :scope "test"]
                          [adzerk/boot-reload "0.2.6" :scope "test"]
                          [deraen/boot-less "0.3.0" :scope "test"]
                          [danielsz/boot-environ "0.0.1"]

                          ;; backend
                          [org.clojure/clojure "1.7.0-beta2"]
                          [org.immutant/immutant "2.0.0"]
                          [ring/ring-defaults "0.1.4"]
                          [compojure "1.3.3"]
                          [environ "1.0.0"]

                          ;; database
                          [yesql "0.4.0"]
                          [org.postgresql/postgresql "9.4-1201-jdbc41"]

                          ;; frontend
                          [org.clojure/clojurescript "0.0-3211"]
                          [quiescent "0.2.0-alpha1"]
                          ])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[deraen.boot-less :refer [less]]
         '[danielsz.boot-environ :refer [environ]])

(task-options!
 cljs {:compiler-options
       {:output-to "reresources/public/js/app.js"
        :output-dir "resources/public/js/out"
        :source-map "resources/public/js/out.js.map"
        :optimizations :none
        :pretty-print  true}}
 aot {:namespace #{'clj-blog.main}}
 jar {:main 'clj-blog.main}
 cljs {:source-map true}
 less {:source-map true})

(deftask wrap-less []
  (comp (less) (sift :move {#"main.css" "resources/public/css/app.css"})))

(deftask dev
  "Start the dev env ..."
  []
  (comp
   (environ :env {:dev true})
   (watch)
   (wrap-less)
   (cljs :optimizations :none :unified-mode true)))

