(ns ribbon.aws
  (:require [ribbon.core :refer [config
                                 send-message
                                 read-message
                                 ack-message
                                 get-message-data
                                 serialize
                                 deserialize]]
            [amazonica.aws.sqs :as sqs]))

(def cred {:access-key ""
           :secret-key ""
           :endpoint "us-west-1"})

(def queue (sqs/find-queue cred "foo"))

(defmethod send-message :aws
  [_ data]
  (sqs/send-message
   cred queue
   (serialize :json data)))

(defmethod read-message :aws
  [_]
  (-> (sqs/receive-message cred queue)
      :messages ;; todo
      first))

(defmethod get-message-data :aws
  [_ message]
  (->> message
       :body
       (deserialize :json)))

(defmethod ack-message :aws
  [_ message]
  (sqs/delete-message cred queue (:receipt-handle message))
  nil)
