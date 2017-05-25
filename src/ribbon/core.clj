(ns ribbon.core
  (:require [amazonica.aws.sqs :as sqs]
            [cognitect.transit :as transit]
            [clojure.data.json :as json])
  (:import [java.io ByteArrayOutputStream]))

(def cfg {:serializer :json})

(def cred {:access-key ""
           :secret-key ""
           :endpoint "us-west-1"})

(def queue (sqs/find-queue cred "foo"))

(defmulti process-action :action)

(defmethod process-action :foo
  [data]
  (assoc data :ready true))

(defmethod process-action :default
  [data]
  (println data)
  nil)

(defn purge-queue [])

(defn dispatch-first-arg
  [& args]
  (first args))

;;
;; (de)serializes
;;
(defmulti serialize dispatch-first-arg)

(defmethod serialize :json
  [_ data]
  (json/write-str data))

(defmulti deserialize dispatch-first-arg)

(defmethod deserialize :json
  [_ string]
  (json/read-str string :key-fn keyword))

(defn read-message
  []
  (-> (sqs/receive-message cred queue)
      :messages ;; todo
      first))

(defn process-message
  [message]
  (let [{:keys [receipt-handle
                message-id
                body
                md5of-body]} message
        data (deserialize (:serializer cfg) body)
        result (process-action data)]
    (sqs/delete-message cred queue receipt-handle)
    result))

(defn worker []
  (when-let [message (read-message)]
    (process-message message)))

(defn send-message
  [data]
  (sqs/send-message
   cred queue
   (serialize (:serializer cfg) data)))
