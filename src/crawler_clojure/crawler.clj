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

(defn abs-url [base link]
  (let [attrs (:attrs link)
        href (:href attrs)]
    (cond (.startsWith href "http") href
          true (str base href))))

(defn print-link [base link depth]
  (let [indent (apply str (repeat depth "  "))
        body (first (:content link))
        href (abs-url base link)
        link-str (cond
                   (= :img (:tag body)) (str indent "<image> -> " href)
                   true (str indent body "->" href))]
    (println link-str)))

(defn crawl
  ([url maxdepth] (crawl url maxdepth 0 []))
  ([url maxdepth current visited]
   (println "---" url "---" visited)
   (when (> maxdepth 0)
     (let [v (cons url visited)
           response (client/get url)
           body (:body response)
           elements (parse body)
           links (find-links elements)]
       ;; (println links))))
       (doseq [link links]
         (print-link url link current))))))
