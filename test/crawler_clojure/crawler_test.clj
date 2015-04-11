(ns crawler-clojure.crawler-test
  (:require [clojure.test :refer :all]
            [crawler-clojure.crawler :refer :all]))

(def relative-link-item {:attrs {:href "/foo"}})
(def abs-link-item {:attrs {:href "http://google.com"}})

(deftest abs-url-test
  (is (= "http://google.com" (abs-url "bar" abs-link-item)))
  (is (= "bar/foo" (abs-url "bar" relative-link-item))))

(deftest abs-link-test
  (is (= "http://google.com" (abs-link "bar" abs-link-item)))
  (is (= "bar/foo" (abs-link "bar" relative-link-item))))
