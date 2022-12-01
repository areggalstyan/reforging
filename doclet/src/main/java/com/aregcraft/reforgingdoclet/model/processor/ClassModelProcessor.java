package com.aregcraft.reforgingdoclet.model.processor;

import com.aregcraft.reforgingdoclet.model.Model;
import com.sun.source.util.DocTrees;
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class ClassModelProcessor extends ModelProcessor {
    private final boolean processSuperclasses;

    public ClassModelProcessor(DocletEnvironment environment, TypeElement element) {
        super(environment, element);
        processSuperclasses = true;
    }

    private ClassModelProcessor(DocTrees docTrees, TypeElement element, Model.Builder modelBuilder) {
        super(docTrees, element, modelBuilder);
        processSuperclasses = false;
    }

    private TypeElement mirrorToElement(TypeMirror type) {
        return (TypeElement) ((DeclaredType) type).asElement();
    }

    @Override
    protected void process() {
        if (processSuperclasses) {
            superclasses().forEach(it -> new ClassModelProcessor(docTrees, it, modelBuilder).process());
        }
        fields().forEach(it -> {
            if (!hasDocTree(it)) {
                modelBuilder.external(name(it), type(it));
                return;
            }
            modelBuilder.property(name(it), type(it), visitDocTree(it));
        });
    }

    private List<VariableElement> fields() {
        return element.getEnclosedElements().stream()
                .filter(VariableElement.class::isInstance)
                .map(VariableElement.class::cast)
                .filter(it -> !it.getModifiers().contains(Modifier.STATIC))
                .filter(it -> !it.getModifiers().contains(Modifier.TRANSIENT))
                .toList();
    }

    private List<TypeElement> superclasses() {
        var superclasses = new ArrayList<TypeElement>();
        var element = this.element;
        while (!"Object".equals(name(element))) {
            element = mirrorToElement(element.getSuperclass());
            superclasses.add(element);
        }
        return superclasses;
    }
}
