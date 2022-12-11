package com.aregcraft.reforging.doclet.model.processor;

import com.aregcraft.reforging.doclet.model.Model;
import com.aregcraft.reforging.doclet.util.Pair;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.util.DocTrees;
import jdk.javadoc.doclet.DocletEnvironment;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public abstract class ModelProcessor {
    private final ModelDocTreeVisitor docTreeVisitor;
    protected final DocTrees docTrees;
    protected final Model.Builder modelBuilder;
    protected final TypeElement element;

    public ModelProcessor(DocletEnvironment environment, TypeElement element) {
        docTreeVisitor = new ModelDocTreeVisitor();
        docTrees = environment.getDocTrees();
        modelBuilder = Model.builder();
        this.element = element;
    }

    protected ModelProcessor(DocTrees docTrees, TypeElement element, Model.Builder modelBuilder) {
        docTreeVisitor = new ModelDocTreeVisitor();
        this.docTrees = docTrees;
        this.modelBuilder = modelBuilder;
        this.element = element;
    }

    public static ModelProcessor create(DocletEnvironment environment, Element element) {
        var type = (TypeElement) element;
        if (type.getRecordComponents().size() == 0) {
            return new ClassModelProcessor(environment, type);
        }
        return new RecordModelProcessor(environment, type);
    }

    protected static String name(Element element) {
        return element.getSimpleName().toString();
    }

    protected static String type(Element element) {
        var type = element.asType().toString().split("\\.");
        return type[type.length - 1];
    }

    public Pair<String, Model> nameModelPair() {
        modelBuilder.description(visitDocTree(element));
        process();
        return new Pair<>(name(element), modelBuilder.build());
    }

    protected boolean hasDocTree(Element element) {
        return docTree(element) != null;
    }

    protected String visitDocTree(Element element) {
        return docTreeVisitor.visitList(docTree(element).getFullBody());
    }

    protected String visitDocTree(DocTree tree) {
        return tree.accept(docTreeVisitor, null);
    }

    protected DocCommentTree docTree(Element element) {
        return docTrees.getDocCommentTree(element);
    }

    protected abstract void process();
}
