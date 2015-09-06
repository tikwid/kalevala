(ns kalevala.core
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [ring.middleware.reload :as reload]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [org.httpkit.server :refer [run-server]]))

(defrecord MapSpace [name location map-layer object-layer player-layer width height])
(defrecord Location [x y])

;(def world (atom {})

;(defn world-assoc [mapspace [x y]]
;  (partial assoc-in [{:x (-> mapspace :location :x)
;                      :y (-> mapspace :location :y)}]
;                    mapspace))

;(def foo (MapSpace. "foo place" (Location. [x y] 10 10)

;(swap! world (world-assoc foo))

(def reload-js
  (str "<script type=\"text/javascript\">
            setTimeout(function () {
              location.reload();
            }, 60*10*3);
        </script>"))

(def autoreload false)

(defn render-view [page]
  (let [head [:head (case autoreload true reload-js
                                     false nil)
                    (include-js "/js/kalevala.js")]
        base [:html head [:body]]
        page-with-body (fn [content] (assoc-in base [2 1] content))]
    (cond (= page :home) (page-with-body "hey."))))

(defroutes app-routes
  (GET "/" [] (html5 (render-view :home)))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn start-server []
  (let [handler (reload/wrap-reload (site #'app-routes))]
    (run-server handler {:port 8080})
    (println "server is running on port 8080")))

(defn -main
  [& args]
  (start-server))
