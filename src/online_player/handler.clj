(ns online-player.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]
             ]
            ))

(def path "resources/public/mp3")

(defn get-file-list[]
  (file-seq (clojure.java.io/file path)))

(defn get-file-names[]
  (filter #(.contains % ".mp3") (map #(.getName %) (get-file-list))))

(defn get-html-list[list]
  (map (fn[c]
         (str "<li onclick='online_player.cljs.click(this)'"
              "data-name='"
              (apply str c)
              "' ><a>"
              (.replace (apply str c) ".mp3" "")
              "</a></li>"))
       list))



(defroutes app-routes
  (GET "/" [] (slurp "resources/public/index.html"))
  (GET "/music-list" [] (apply str (get-html-list (get-file-names))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))


(defn -main [& args]
  (print "Hello world")
  )
