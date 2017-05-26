(ns ribbon.proto)

(defprotocol IQueue
  (init [this])
  (connect [this])
  (disconnect [this])
  (send-message [this data])
  (read-message [this])
  (process-message [this message])
  (ack-message [this message])
  (purge [this])
  (start [this])
  (stop [this]))
