(ns online-player.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            ))

(def path "resources/public/mp3")

(defn get-file-list[]
  (file-seq (clojure.java.io/file path)))

(defn get-file-names[]
  ;(filter #(.contains % ".mp3") (map #(.getName %) (get-file-list)))
  (->>(get-file-list)
      (map #(.getName %))
      (filter #(.contains % ".mp3"))))



(defn get-html-list[list]
  (map (fn[c]
         (let [names (apply str c)]
          (str "<li " ;;"<li onclick='online_player.cljs.click(this)'"
               "data-name='" names "' ><a>"
               (.replace names ".mp3" "")
               "</a></li>")))
       list))

(defroutes app-routes
  (GET "/" [] (slurp "resources/public/index.html"))
  (GET "/music-list" [] (apply str (get-html-list (get-file-names))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [& args]
  (jetty/run-jetty app-routes {:port 8081})
  )
