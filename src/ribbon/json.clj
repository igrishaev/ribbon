(ns ribbon.json
  (:require [clojure.data.json :as json]
            [ribbon.core :refer [config
                                 serialize
                                 deserialize]]))

(defmethod serialize :json
  [_ data]
  (json/write-str data))

(defmethod deserialize :json
  [_ string]
  (json/read-str string :key-fn keyword))
