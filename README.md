# online-player

后台：Clojure提供以保存在resources/public/mp3/目录下的
mp3文件名的列表

* ```<li data-name='file url.mp3'> <a>file name </a></li>```
* 然后前台用ClojureScript绑定单击事件
* 单机后把li元素的data-name属性值设置到Audio的src属性
* 播放接受后(audio有onended事件可以帮到play-next-song)下一曲...
* 最后一曲播放完毕后从第一个开始

### 获取文件列表
```
(def path "resources/public/mp3")
(defn get-file-names[p]
  (->>(file-seq (clojure.java.io/file p))
      (map #(.getName %))
      (filter #(.contains % ".mp3"))))
```
### 生成list元素
```
(defn url-encode [string]
  (some->
   string
   str (URLEncoder/encode "UTF-8") (.replace "+" "%20")))

(defn get-li-item [names]
  (str "<li data-name='" (url-encode names) "' ><a>"
       (.replace names ".mp3" "")
       "</a></li>")
  )

```
### 嵌入的技术：
+ JQuery
+ BootStrap

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2015 FIXME
