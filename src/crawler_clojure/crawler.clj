(ns crawler-clojure.crawler
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [cemerick.url :refer [url]]
            [crawler-clojure.deep-merge :refer [deep-merge]]))

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

(defn link-from-url [url]
  {:attrs {:href url}})

(defn url-from-link [link]
  (let [attrs (:attrs link)
        href (:href attrs)]
    href))

(defn abs-link [base link]
  (let [href (url-from-link link)]
    (if (not (.startsWith (str href) "http"))
      (deep-merge link (link-from-url (str (url base href))))
      (deep-merge link (link-from-url (str (url href)))))))

(defn abs-url [base link]
  (let [link (abs-link base link)
        href (url-from-link link)]
    href))

(defn print-link [link depth]
  (let [indent (apply str (repeat depth "  "))
        body (first (:content link))
        attrs (:attrs link)
        href (:href attrs)
        link-str (cond
                   (= :img (:tag body)) (str indent "<image> -> " href)
                   true (str indent body " -> " href))]
    (println link-str)))

(defn collect-links [url depth]
  (let [response (client/get url)
        body (:body response)
        elements (parse body)
        links (find-links elements)]
    (map (fn [l]
           (let [with-depth (assoc l :depth depth)
                 abs (abs-link url with-depth)]
             abs)) links)))

(defn crawl
  ([url maxdepth] (crawl {:depth 0 :attrs {:href url}} maxdepth 0 [] []))
  ([link maxdepth current links visited]
   (when link
     (let [url (url-from-link link)
           depth (or (:depth link) 0)]
       ;; (println "---" depth link maxdepth current visited)
       ;; (println (count links))
       (if (contains? visited url)
         nil ;; do nothing if visited
         (when (<= depth maxdepth)
           ;; (println depth maxdepth)
           (print-link link depth)
           (let [visited (conj visited url)]
             (if (< depth maxdepth)
               ;; We can go at least once more, collect links
               (let [l (collect-links url (inc current))
                     links (apply conj links l)]
                 (recur (first links)
                        maxdepth
                        (inc current)
                        (vec (rest links))
                        visited))
               ;; Our children will be ignored, don't bother collecting
               (recur (first links)
                      maxdepth
                      (inc current)
                      (vec (rest links))
                      visited)))))))))
