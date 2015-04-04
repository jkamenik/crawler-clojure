(ns crawler-clojure.core
  (:require
   [clojure.tools.cli :refer [parse-opts]]
   [crawler-clojure.crawler :as crawler])
  (:gen-class))

(def cli-options
  [["-h" "--help" "Show help"]
   ["-d" "--depth NUM" "Max depth" :default 1]])

(defn usage [opts]
  (println "crawler [options] <URL>")
  (println "")
  (println " Crawls a URL for links, then follows those links")
  (println "")
  (println "Options:")
  (println opts)
  (println "")
  (println "Example:")
  (println "  crawler http://somedomain.com")
  (println "  Home -> /")
  (println "  About Us -> /about_us.php")
  (println "  Careers -> http://otherdomain.com/somedomain.com")
  (println "    Home -> http://somedomain.com")
  (println "    Careers -> /somedomain.com")
  (System/exit 1))

(defn -main [& args]
  ;; (println (parse-opts args cli-options))
  (let [{:keys [options arguments errors summary]}
        (parse-opts args cli-options)]
    (cond
      (:help options) (usage summary)
      errors (do (println errors)
                 (System/exit 1))
      ;; true (do
      ;;        (println options)
      ;;        (println arguments)
      ;;        (println errors)
      ;;        (println summary)))))
      true (doall
            (map crawler/crawl arguments (repeatedly #(:depth options)))))))
