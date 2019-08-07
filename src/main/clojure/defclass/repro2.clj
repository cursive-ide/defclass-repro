(ns defclass.repro2
  (:require [clojure.interop :refer [extend-class]])
  (:import (com.intellij.execution.configurations GeneralCommandLine)
           (com.intellij.execution.process DefaultJavaProcessHandler ProcessTerminatedListener ProcessAdapter ProcessEvent OSProcessHandler)
           (com.intellij.util.io BaseOutputReader$Options)))

(defn works []
  (let [command-line (GeneralCommandLine. ["/usr/bin/env" "java"
                                           "-cp" (str "/Users/colin/.m2/repository/com/cursive-ide/clojure/1.10.1/clojure-1.10.1.jar:"
                                                      "/Users/colin/.m2/repository/org/clojure/spec.alpha/0.2.176/spec.alpha-0.2.176.jar:"
                                                      "/Users/colin/.m2/repository/org/clojure/core.specs.alpha/0.2.44/core.specs.alpha-0.2.44.jar")
                                           "clojure.main" "-e" ":hello-world"])
        process-handler (DefaultJavaProcessHandler. command-line)]
    (ProcessTerminatedListener/attach process-handler)
    (.addProcessListener process-handler (proxy [ProcessAdapter] []
                                           (onTextAvailable [event outputType]
                                             (print (.getText ^ProcessEvent event)))))
    (.startNotify process-handler)
    (.waitFor process-handler)))

(defn doesnt-work []
  (let [command-line (GeneralCommandLine. ["/usr/bin/env" "java"
                                           "-cp" (str "/Users/colin/.m2/repository/com/cursive-ide/clojure/1.10.1/clojure-1.10.1.jar:"
                                                      "/Users/colin/.m2/repository/org/clojure/spec.alpha/0.2.176/spec.alpha-0.2.176.jar:"
                                                      "/Users/colin/.m2/repository/org/clojure/core.specs.alpha/0.2.44/core.specs.alpha-0.2.44.jar")
                                           "clojure.main" "-e" ":hello-world"])
        process-handler (extend-class DefaultJavaProcessHandler [command-line]
                          (readerOptions [this]
                            (BaseOutputReader$Options/forMostlySilentProcess)))]
    (ProcessTerminatedListener/attach process-handler)
    (.addProcessListener process-handler (proxy [ProcessAdapter] []
                                           (onTextAvailable [event outputType]
                                             (print (.getText ^ProcessEvent event)))))
    (.startNotify process-handler)
    (.waitFor process-handler)))

(defn -main [& args]
  (works))
