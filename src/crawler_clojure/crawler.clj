(ns crawler-clojure.crawler
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [clojure.string :refer [trim]]
            [cemerick.url :refer [url]]
            [crawler-clojure.deep-merge :refer [deep-merge]]))

(defn debug[& args])
  ;; (apply println "---" args))

(defn find-links [html]
  (let [links (html/select html [[:a (html/attr? :href)]])]
    (filter (fn [link]
              (let [body (first (:content link))
                    attrs (:attrs link)
                    href  (:href attrs)]
                (not (or (= nil body)
                         (= "#" href)
                         (= nil href)))))
            links)))

(defn parse [s]
  (if s
    (html/html-resource
     (java.io.StringReader. s))
    []))

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
                   (= :stong (:tag body)) (str indent
                                               (trim (str (first (:content link)))) " -> " href)
                   true (str indent (trim (str body)) " -> " href))]
    (println link-str)))

(defn collect-links [url depth]
  (let [response (try (client/get url)
                      (catch Exception e ""))
        body (:body response)
        elements (parse body)
        links (find-links elements)]
    (map (fn [l]
           (let [with-depth (assoc l :depth depth)
                 abs (abs-link url with-depth)]
             abs)) links)))


(def visited (atom (set ())))

(defn rcrawl [links maxdepth]
  (doseq [link links]
    (let [url (url-from-link link)
          depth (or (:depth link) 0)]
      (print-link link depth)
      (when (not (contains? @visited url))
        (swap! visited conj url)
        (when (< depth maxdepth)
          (let [ls (collect-links url (inc depth))]
            (rcrawl ls maxdepth)))))))

(defn crawl [url maxdepth]
  (rcrawl [{:depth 0 :attrs {:href url}}] maxdepth))
