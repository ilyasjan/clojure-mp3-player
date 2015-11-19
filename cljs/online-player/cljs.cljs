(ns online-player.cljs
  (:require [goog.events :as events]
            [goog.dom :as dom]))
;; alert
(defn  alert[c]
  (js/alert c))
;; prompit
(defn prompit[t]
  (js/prompt t))
;; write log to browser console
(defn log[c]
  (.log js/console c))
;; get or set attribute
(defn attr
  ([el at](.getAttribute el at))
  ([el at val](.setAttribute el at val))
  )


(defn make-js-map
  "makes a javascript map from a clojure one"
  [cljmap]
  (let [out (js-obj)]
    (doall (map #(aset out (name (first %)) (second %)) cljmap))
    out))

;; support JQuery
(def jquery (js* "$"))

;; Ajax request
(defn ajax [url fun]
  (let [req (js/XMLHttpRequest.)]
    (aset req "onreadystatechange"
          (fn []
            (when (= (.-readyState req) 4)
              (fun req))))
    (.open req "GET" url true)
    (.send req ""))
  )

(defn ajax-with-jquery[u f]
  (.ajax jquery
         (make-js-map {:url u :success f})))


(def player (dom/getElement "player"))
(def display (dom/getElement "display-content"))
(def s-class "selected")
(def at "data-name")

(defn get-path[atn]
  (str "./mp3/" atn))

;;Click events
(defn click[e]
  (let [el (.-currentTarget e)
        mn (.html (.find (jquery el) "a"))
        mp (get-path (attr el at))]
    (-> (jquery "li")
        (.removeClass s-class))
    (.pause player)
    (attr player "src" mp)
    (.html (jquery display) mn)
    (.play player)
    (attr el "class" s-class)
    ))


;;set html content to current id object
(defn set-html[id html]
  (set! (.-innerHTML (dom/getElement id)) html)
  )

(defn bind-event[]
  (set! (.-onended player) #(alert "Soga")))

;; Ajax call back function
(defn call-back[r]
  (do (set-html "list" r)
      (.click (jquery "li") click)
      ;;(bind-event)
   ))



;; send Ajax request to server,get music list,and set to html
(defn main[]
  (ajax-with-jquery "./music-list" call-back))

;; main function
(main)
