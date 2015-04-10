(ns crawler-clojure.crawler
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]))

(defn find-links [html]
  (let [links (html/select html [[:a (html/attr? :href)]])]
    (filter (fn [link]
              (let [body (first (:content link))
                    attrs (:attrs link)
                    href  (:href attrs)]
                (not (or (= nil body) (= "#" href)))))
            links)))

(defn parse [s]
  (html/html-resource
   (java.io.StringReader. s)))

(defn print-link [link]
  (let [body (first (:content link))
        attrs (:attrs link)
        href (:href attrs)]
    (cond
      (= :img (:tag body)) (println "<image> -> " href)
      true (println body "->" href))))

(defn abs-url [base link]
  (let [attrs (:attrs link)
        href (:href attrs)]
    (cond (.startsWith href "http") href
          true (str base href))))

(defn crawl [url maxdepth visited]
  (println "---" url "---" visited)
  (when (> maxdepth 0)
    (let [;;visited (cons url visited)
          response (client/get url)
          body (:body response)
          elements (parse body)
          links (find-links elements)]
      ;; (println links))))
      (doseq [link links]
        (print-link link)
        (println (str "---" (abs-url url link)))))))
