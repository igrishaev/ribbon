(ns ribbon.core)

(def config {:serializer :json})

(defn dispatch-first-arg
  [& args]
  (first args))

(defmulti process-action :action)

(defmethod process-action :default
  [data]
  data)

(defmulti purge-queue dispatch-first-arg)

(defmulti serialize dispatch-first-arg)

(defmulti deserialize dispatch-first-arg)

(defmulti read-message dispatch-first-arg)

(defmulti ack-message dispatch-first-arg)

(defmulti send-message dispatch-first-arg)

(defmulti get-message-data dispatch-first-arg)

(defn process-message
  [message]
  (let [data (get-message-data :aws message)
        result (process-action data)]
    (ack-message :aws message)
    result))

(defn worker []
  (when-let [message (read-message :aws)]
    (process-message message)))
