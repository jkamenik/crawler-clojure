(ns crawler-clojure.crawler-test
  (:require [clojure.test :refer :all]
            [crawler-clojure.crawler :refer :all]))

(def relative-link-item {:attrs {:href "/foo"}})
(def abs-link-item {:attrs {:href "http://google.com"}})

(deftest abs-url-test
  (is (= "http://google.com" (abs-url "https://bar" abs-link-item)))
  (is (= "https://bar/foo" (abs-url "https://bar" relative-link-item))))

(deftest abs-link-test
  (is (= {:attrs {:href "http://google.com"}} (abs-link "https://bar" abs-link-item)))
  (is (= {:attrs {:href "https://bar/foo"}} (abs-link "https://bar" relative-link-item))))
