package com.aregcraft.reforging.doclet.util;

import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public record ElementFinder(DocletEnvironment environment) {
    private static boolean hasAnnotation(Element element, String annotation) {
        return element.getAnnotationMirrors().stream()
                .map(AnnotationMirror::getAnnotationType)
                .map(DeclaredType::asElement)
                .map(Element::getSimpleName)
                .map(Object::toString)
                .anyMatch(annotation::equals);
    }

    public List<? extends Element> byAnnotation(String annotation) {
        return environment.getSpecifiedElements().stream().filter(it -> hasAnnotation(it, annotation)).toList();
    }
}
