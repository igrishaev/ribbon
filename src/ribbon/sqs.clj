(ns ribbon.sqs
  (:require [ribbon.proto :refer [IQueue]]
            [amazonica.aws.sqs :as sqs]))

(defprotocol ISerializer
  (serialize [_ data])
  (deserialize [_ data]))

(defrecord JSON [foo bar]
  ISerializer
  (serialize [this data]
    (assoc this :foo 42))
  (deserialize [_ data]
    "foo"))

(defrecord SQS [access-key
                secret-key
                region
                serializer
                action-handler
                queue-name
                queue-url]

  IQueue

  (init [this]
    (let [creds {:access-key access-key
                 :secret-key secret-key
                 :endpoint region}
          query-url (:todo queue-name)]

      (assoc this
             :queue-url queue-url
             :creds creds)))

  (connect [this]
    this)

  (disconnect [this]
    this)

  (send-message [this data]
    (sqs/send-message
     (:creds this)
     (:queue-url this)
     (serialize serializer data)))

  (disconnect [this]
    this)

  (start [this]
    (let [f (future
              (loop []
                (when-let [message (read-message this)]
                  (process-message this message)
                  (ack-message this message))
                (recur)))]
      (assoc this :f f)))

  (stop [this]
    (when-let [f (:f this)]
      (future-cancel f)
      (dissoc this :f)))

  (ack-message [this message]
    (sqs/delete-message
     (:creds this)
     (:queue-url this)
     (:receipt-handle message)))

  (process-message [this message]
    (let [body (:body message)
          data (deserialize serializer data)]
      (action-handler data)))

  (read-message [this]
    (let [result (sqs/receive-message
                  (:creds this)
                  (:queue-url))]
      (-> result
          :messages
          first))))
