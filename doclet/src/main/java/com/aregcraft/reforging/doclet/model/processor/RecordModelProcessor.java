package com.aregcraft.reforging.doclet.model.processor;

import com.sun.source.doctree.ParamTree;
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

public class RecordModelProcessor extends ModelProcessor {
    public RecordModelProcessor(DocletEnvironment environment, TypeElement element) {
        super(environment, element);
    }

    @Override
    protected void process() {
        components().forEach(it -> modelBuilder.property(name(it), type(it), param(name(it))));
    }

    private List<? extends RecordComponentElement> components() {
        return element.getRecordComponents();
    }

    private String param(String name) {
        return docTree(element).getBlockTags().stream()
                .filter(ParamTree.class::isInstance)
                .map(ParamTree.class::cast)
                .filter(it -> visitDocTree(it.getName()).equals(name))
                .map(this::visitDocTree)
                .findAny().orElseThrow();
    }
}
