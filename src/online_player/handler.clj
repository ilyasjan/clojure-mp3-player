(ns online-player.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]
            ]
            ))

(def path "resources/public/mp3")

(defn get-file-list[]
  (file-seq (clojure.java.io/file path)))

(defn get-file-names[]
  ;(filter #(.contains % ".mp3") (map #(.getName %) (get-file-list)))
  (->>(get-file-list)
      (map #(.getName %))
      (filter #(.contains % ".mp3"))
   )
  )

(defn get-html-list[list]
  (map (fn[c]
         (str "<li onclick='online_player.cljs.click(this)'"
              "data-name='"
              (apply str c)
              "' ><a>"
              (.replace (apply str c) ".mp3" "")
              "</a></li>"))
       list))


(defn one[a b]
  (print "1. " a b "\n")
  "'I come from one'")

(defn three[a b]
  (print "3. " a b "\n")
  )

(defn two[a b]
  (print "2. " a b "\n")
  "'I come from two'")

(print "for outside: " (doto "I come from top."
                                 (one "for one.")
                                 (two "for two.")
                                 (three "for three.")))


(defroutes app-routes

  (GET "/" [] (slurp "resources/public/index.html"))
  ;;(resource "resources/public")
  (GET "/music-list" [] (apply str (get-html-list (get-file-names))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [& args]
  (jetty/run-jetty app-routes {:port 8081})
  )
