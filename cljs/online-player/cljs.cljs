(ns online-player.cljs
  (:require [goog.events :as events]
            [goog.dom :as dom]))

;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;
;;;Global functions
(defn  alert[c]
  "javascript alert"
  (js/alert c))

(defn log[c]
  "console log"
  (.log js/console c))

(defn attr
  "set attribute or get attribute"
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

(defn set-html[id html]
  (.html (jquery (str "#" id)) html)
  )
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; there is about player
(def player (dom/getElement "player"))
(def display (dom/getElement "display-content"))
(def at "data-name")

(defn  player-src[p]
  (attr player "src" p))

(defn display-text[n]
  (.html (jquery display) n))

(defn update-selected[el]
  "remove selected class,add to current element"
  (do (-> (jquery "li")
          (.removeClass "selected"))
      (attr el "class" "selected")))

(defn get-music-name[el]
  "get music name from in the elements children a tag"
  (.html (.find (jquery el) "a"))
  )

(defn get-music-src[el]
  (str "./mp3/" (attr el at))
  )

(defn play-song[index]
  (let [el (aget (jquery (str "li:eq(" index ")")) 0)
        mn (get-music-name el)
        mp (get-music-src el)]
    (.pause player)
    (update-selected el)
    (player-src mp)
    (display-text mn)
    (.play player)
    ))

(defn play-next-song[]
  "on current song ended,let's start play next one"
  (let [cs (.index (jquery "li.selected"))
        len (dec (.-length (jquery "li")))]
    (if (< cs len)
      (play-song (inc cs))
      (play-song "0"))
    ))

(defn click[e]
  "when li item clicked,play current items data-names song"
  (let [el (.-currentTarget e)]
    (play-song (.index (jquery el)))
    )
  )

(defn bind-event[]
  "for end playing bind event"
  (events/listen player "ended" play-next-song))

(defn call-back[con]
  "when ajax request have response,call this function"
  (do
    (set-html "list" con)
    (.click (jquery "li") click)
    (bind-event)
    ))

(defn main[]
  (ajax-with-jquery "./music-list" call-back))

(main)
