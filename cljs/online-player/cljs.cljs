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



(defn li-click[el]
  (let [at "data-name"
        player (dom/getElement "player")
        display (dom/getElement "display-content")
        music-name (.-innerHTML (aget (.getElementsByTagName el "a") 0))
        music-path (str "./mp3/" (apply str (attr el at)))
        coll (js/document.getElementsByTagName "li")
        i-coll (range (.-length coll))
        ]

    (clj->js (map
      (fn[i](let [e (aget coll i)]
              (attr e "class" "")))
      i-coll))
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

;; send Ajax request to server,get music list,and set to html
(defn main[]
  (ajax "./music-list"
        (fn[r]
          (let [cll (js/document.getElementsByTagName "li")]
            (set-html "list" (.-responseText r))))))

;;call main function

(main)
(events/listen (dom/getElement "display-content") "click"  (fn[e](alert "help")))


;(do (let [coll (js/document.getElementsByTagName "li")
 ;         i-coll (range (.-length coll))]
  ;    (map (fn[i](let[e (aget coll i)](set! (.-onclick e) li-click))) i-coll)
   ;   ))
