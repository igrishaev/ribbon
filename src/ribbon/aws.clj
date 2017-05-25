(ns ribbon.aws
  (:require [ribbon.core :refer [send-message
                                 read-message
                                 ack-message
                                 get-message-data
                                 serialize
                                 deserialize]]
            [amazonica.aws.sqs :as sqs]))

;; (def queue (sqs/find-queue cred "foo"))

(defmethod send-message :aws
  [config data & args]
  (apply sqs/send-message
         (-> config :aws :cred)
         (-> config :aws :queue-url)
         (serialize config data)
         args))

(defmethod read-message :aws
  [config & args]
  (-> sqs/receive-message
      (apply
       (-> config :aws :cred)
       (-> config :aws :queue-url)
       args)
      :messages ;; todo
      first))

(defmethod get-message-data :aws
  [config message]
  (->> message
       :body
       (deserialize config)))

(defmethod ack-message :aws
  [config message]
  (sqs/delete-message
   (-> config :aws :cred)
   (-> config :aws :queue-url)
   (:receipt-handle message))
  nil)
