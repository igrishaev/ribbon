(ns ribbon.core)

(def config {:serializer :json
             :backend :aws
             :action :action
             :aws {:cred {:access-key ""
                          :secret-key ""
                          :endpoint "us-west-1"}
                   :queue-url "https://sqs.us-west-1.amazonaws.com/"}})

(defn dispatch-backend
  [config & _]
  (:backend config))

(defn dispatch-serializer
  [config & _]
  (:serializer config))

(defn dispatch-action
  [config & _]
  (:action config))

(defmulti process-action dispatch-action)

(defmethod process-action :default
  [data]
  data)

(defmethod process-action 42
  [data]
  424242)

(defmulti serialize dispatch-serializer)

(defmulti deserialize dispatch-serializer)

(defmulti purge-queue dispatch-backend)

(defmulti read-message dispatch-backend)

(defmulti ack-message dispatch-backend)

(defmulti send-message dispatch-backend)

(defmulti get-message-data dispatch-backend)

(defn process-message
  [config message]
  (let [data (get-message-data config message)
        result (process-action data)]
    (ack-message config message)
    result))

(defn worker [config]
  (when-let [message (read-message config)]
    (process-message config message)))
