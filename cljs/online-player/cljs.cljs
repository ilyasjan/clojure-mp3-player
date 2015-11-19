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
  (.ajax (jquery)))


;;Click events
(defn click[e]
  (let [el (.-currentTarget e)
        at "data-name"
        player (dom/getElement "player")
        display (dom/getElement "display-content")
        music-name (.-innerHTML (aget (.getElementsByTagName el "a") 0))
        music-path (str "./mp3/" (apply str (attr el at)))
        coll (js/document.getElementsByTagName "li")
        i-coll (range (.-length coll))
        ]
    (-> (jquery "li")
        (.removeClass "Selected"))
    (.pause player)
    (attr player "src" music-path)
    (set! (.-innerText display) music-name)
    (.play player)
    (attr el "class" "selected")
    ))


;;set html content to current id object
(defn set-html[id html]
  (set! (.-innerHTML (dom/getElement id)) html)
  )

;; Ajax call back function
(defn call-back[r]
  (let [cll (js/document.getElementsByTagName "li")]
    (set-html "list" (.-responseText r))
    (->
     (jquery "li")
     (.click click))
    ))

;; send Ajax request to server,get music list,and set to html
(defn main[]
  (ajax "./music-list" call-back))

;;call main function
(main)
