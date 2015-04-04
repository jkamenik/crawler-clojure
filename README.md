# Crawler for Clojure

[![Build Status](https://travis-ci.org/jkamenik/crawler-clojure.svg?branch=master)](https://travis-ci.org/jkamenik/crawler-clojure)

A simple web crawler

## Setup

Install Java 7, and Leinigen then run the following:

```bash
$ lein run crawler <args>
```

## Challenge

The goal is to provide a tool that takes a single command line
argument of a URL and determine the content of that URL after crawling
it.

The following requirements apply to this challenge:

1. The tool must download the HTML
1. The tool must parse and print all the links found in that HTML
1. The tool must allow for an optional depth argument (default 2)
   which will control how many pages it will crawl for links.
1. The output should be the link's text followed by the link url (see
   below).
1. A reasonable exit code needs to be provided if the main URL is not
   accessible; 2nd level URL errors can be ignored.

```bash
$ lein run crawler http://somedomain.com
Home -> /
About Us -> /about_us.php
Careers -> http://otherdomain.com/somedomain.com
  Home -> http://somedomain.com
  Careers -> /somedomain.com
```

Extra credit (optional)
* Parallelize the downloading, and parsing, and collecting of links
* Follow redirects of any page
* Add debugging which is off by default and can be enabled with "-v"
  * Control the level of debugging by repeating "-v" (i.e., "-vvvv")
* Save the HTML in a folder matching the link title
* Save any resources used the by page: CSS, JS, and Images.
  * Rewrite the links and references in the HTML to be relative file paths
* Enable Javascript, using Selenium Webdriver, or similar
