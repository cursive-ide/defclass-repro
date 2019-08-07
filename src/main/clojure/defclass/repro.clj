(ns defclass.repro
  (:require [clojure.interop :refer [defclass extend-class]])
  (:import (com.intellij.psi PsiElementVisitor)
           (com.intellij.codeInspection LocalInspectionTool)))

(defclass UnusedRequire []
  :load-ns true
  LocalInspectionTool []
  (buildVisitor [_ holder isOnTheFly]
    (extend-class PsiElementVisitor []
      (visitElement [_ element]
        nil))))
