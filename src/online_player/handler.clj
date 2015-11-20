(ns online-player.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            )
  (:import (java.net URLEncoder))
)

(def path "resources/public/mp3")

(defn get-file-names[p]
  (->>(file-seq (clojure.java.io/file p))
      (map #(.getName %))
      (filter #(.contains % ".mp3"))))

(defn url-encode [string]
  (some->
   string
   str (URLEncoder/encode "UTF-8") (.replace "+" "%20")))

(defn get-li-item [names]
  (str "<li data-name='" (url-encode names) "' ><a>"
       (.replace names ".mp3" "")
       "</a></li>")
  )

(defn get-html-list[list]
  (map #(get-li-item (apply str %)) list))

(defroutes app-routes
  (GET "/" [] (slurp "resources/public/index.html"))
  (GET "/music-list" [] (apply str (get-html-list (get-file-names path))))
  (route/not-found "Not Found"))


(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [& args]
  (jetty/run-jetty app-routes {:port 8081})
  )
