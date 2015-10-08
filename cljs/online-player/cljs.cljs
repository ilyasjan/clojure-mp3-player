(ns online-player.cljs
  (:require [goog.events :as events]
            [goog.dom :as dom])
  )

(defn ajax [url fun]
  (let [req (js/XMLHttpRequest.)]
    (aset req "onreadystatechange"
          (fn []
            (when (= (.-readyState req) 4)
              (fun req))))
    (.open req "GET" url true)
    (.send req ""))
  )


(defn  alert[c]
  (js/alert c))

(defn prompit[t]
  (js/prompt t))
(defn log[c]
  (.log js/console c)
)

;;(defn gattr
;;  [el at] (.getAttribute el at)
;;  )

;;(defn sattr
;;  [el at val](.setAttribute el at val)
;;  )

(defn attr
  ([el at](.getAttribute el at))
  ([el at val](.setAttribute el at val))
  )


(defn ^:export click[el]
  (let [at "data-name"
        player (dom/getElement "player")
        display (dom/getElement "display-content")
        music-name (.-innerHTML el)
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

(defn set-html[id html]
  (set! (.-innerHTML (dom/getElement id)) html)
  )

(defn main[]
  (ajax "./music-list"
        (fn[r]
          (let [cll (js/document.getElementsByTagName "li")]
            (set-html "list" (.-responseText r))))))


;;;call the main
(main)
