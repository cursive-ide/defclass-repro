(ns defclass.repro3
  (:require [clojure.interop :refer [defclass]])
  (:import (com.intellij.psi PsiDocumentManager EmptySubstitutor PsiClass)
           (com.intellij.codeInsight.lookup LookupItem)
           #_(com.intellij.codeInsight.completion JavaPsiClassReferenceElement)))

; Stubs to make code compile

(defn class-name [class])

(defn document-from [context])

(defn element-at [context offset])

(defn symbol? [form])

(defn parent [form])

(defn add-import [element fqn])

(defn class-fqn [class])

(defn reference [element])

(defclass ClassLookupItem [class]
  LookupItem [class (class-name class)]
  (renderElement [this presentation]
    (.renderElement ^LookupItem this presentation)
    (.forceQualify ^LookupItem this)
    #_(JavaPsiClassReferenceElement/renderClassItem presentation this class false
                                                    (or (.getLocationString (.getPresentation ^PsiClass class)) "<unknown>")
                                                    (EmptySubstitutor/getInstance)))
  (handleInsert [this context]
    (-> (PsiDocumentManager/getInstance (.getProject context))
        (.commitDocument (document-from context)))
    (let [element (element-at (.getFile context) (.getStartOffset context))
          element (if (symbol? (parent element))
                    (parent element)
                    element)]
      (when (symbol? element)
        (add-import element (class-fqn class))
        (.bindToElement (reference element) class)))))
