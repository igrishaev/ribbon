(ns ribbon.example
  (:require [ribbon.core :refer [config
                                 send-message
                                 read-message
                                 worker]]
            [ribbon.aws]
            [ribbon.json]))
