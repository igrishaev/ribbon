(ns ribbon.transit
  (:require [cognitect.transit :as transit]
            [ribbon.core :refer [config
                                 serialize
                                 deserialize]])
  (:import [java.io ByteArrayOutputStream]))

(defmethod serialize :transit
  [_ data]
  (throw (Exception. "not implemented"))
  ;;
  )

(defmethod deserialize :transit
  [_ string]
  (throw (Exception. "not implemented")))
