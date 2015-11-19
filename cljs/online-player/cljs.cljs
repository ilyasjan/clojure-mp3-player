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

(defn ajax-with-jquery[u f]
  (.ajax jquery
         (make-js-map {:url u :success f})))


(def player (dom/getElement "player"))
(def display (dom/getElement "display-content"))
(def s-class "selected")
(def at "data-name")

(defn get-path[atn]
  (str "./mp3/" atn))

(defn music-path[p]
  (attr player "src" p))

(defn display-text[n]
  (.html (jquery display) n)
  )

(defn clear-selected[]
  (-> (jquery "li")
      (.removeClass s-class)))

(defn make-select[el]
  (attr el "class" s-class))

(defn update-selected[el]
  (do (clear-selected)
      (make-select el)))

(defn set-html[id html]
  (set! (.-innerHTML (dom/getElement id)) html)
  )


(defn get-music-name[el]
  (.html (.find (jquery el) "a"))
  )

(defn get-music-path[el]
  (get-path (attr el at))
  )


(defn play-song[sc]
  (let [el (aget (jquery (str "li:eq("sc ")")) 0)
        mn (get-music-name el)
        mp (get-music-path el)]
    (update-selected el)
    (.pause player)
    (music-path mp)
    (display-text mn)
    (.play player)
    ))

(defn play-next-song[]
  "when playing end,let's play next song"
  (let [cs (.index (jquery "li.selected"))
        len (.-length (jquery "li"))]
    (if (< cs len)
      (play-song (inc cs))
      (play-song 0))
    ))

(defn onend[]
  (play-next-song))

(defn click[e]
  (let [el (.-currentTarget e)]
    (play-song (.index (jquery el)))
   )
  )

(defn bind-event[]
  (events/listen player "ended" onend))

;; Ajax call back function
(defn call-back[r]
  (do (set-html "list" r)
      (.click (jquery "li") click)
      (bind-event)
   ))

;; send Ajax request to server,get music list,and set to html
(defn main[]
  (ajax-with-jquery "./music-list" call-back))

;; main function
(main)
