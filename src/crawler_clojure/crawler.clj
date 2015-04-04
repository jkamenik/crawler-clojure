(ns crawler-clojure.crawler
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]))

(defn find-links [html]
  (html/select html [[:a (html/attr? :href)]]))

(defn parse [s]
  (html/html-resource
   (java.io.StringReader. s)))

(defn print-link [link]
  (let [body (first (:content link))
        attrs (:attrs link)
        href (:href attrs)]
    (cond
      (= nil body) nil
      (= "#" href) nil
      (= :img (:tag body)) (println "<image> -> " href)
      true (println body "->" href))))

(defn crawl [url maxdepth]
  (println "---" url "---")
  (when (> maxdepth 0)
    (let [response (client/get url)
          body (:body response)
          elements (parse body)
          links (find-links elements)]
      ;; (println links)
      (doall (map print-link links)))))
