package com.aregcraft.reforging.doclet.model.processor;

import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.IdentifierTree;
import com.sun.source.doctree.ParamTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.util.SimpleDocTreeVisitor;

import java.util.List;
import java.util.Objects;

public class ModelDocTreeVisitor extends SimpleDocTreeVisitor<String, Void> {
    @Override
    public String visitText(TextTree node, Void unused) {
        return node.getBody();
    }

    @Override
    public String visitParam(ParamTree node, Void unused) {
        return visitList(node.getDescription());
    }

    @Override
    public String visitIdentifier(IdentifierTree node, Void unused) {
        return node.getName().toString();
    }

    public String visitList(List<? extends DocTree> list) {
        return String.join("", list.stream().map(it -> it.accept(this, null))
                .filter(Objects::nonNull).toList()).replaceAll("\n", "");
    }
}
