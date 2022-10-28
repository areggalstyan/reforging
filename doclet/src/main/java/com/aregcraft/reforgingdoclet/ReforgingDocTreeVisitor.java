package com.aregcraft.reforgingdoclet;

import com.sun.source.doctree.*;

public class ReforgingDocTreeVisitor implements DocTreeVisitor<String, Void> {
    @Override
    public String visitAttribute(AttributeTree node, Void unused) {
        return null;
    }

    @Override
    public String visitAuthor(AuthorTree node, Void unused) {
        return null;
    }

    @Override
    public String visitComment(CommentTree node, Void unused) {
        return null;
    }

    @Override
    public String visitDeprecated(DeprecatedTree node, Void unused) {
        return null;
    }

    @Override
    public String visitDocComment(DocCommentTree node, Void unused) {
        return null;
    }

    @Override
    public String visitDocRoot(DocRootTree node, Void unused) {
        return null;
    }

    @Override
    public String visitEndElement(EndElementTree node, Void unused) {
        return null;
    }

    @Override
    public String visitEntity(EntityTree node, Void unused) {
        return null;
    }

    @Override
    public String visitErroneous(ErroneousTree node, Void unused) {
        return null;
    }

    @Override
    public String visitIdentifier(IdentifierTree node, Void unused) {
        return null;
    }

    @Override
    public String visitInheritDoc(InheritDocTree node, Void unused) {
        return null;
    }

    @Override
    public String visitLink(LinkTree node, Void unused) {
        return null;
    }

    @Override
    public String visitLiteral(LiteralTree node, Void unused) {
        return null;
    }

    @Override
    public String visitParam(ParamTree node, Void unused) {
        return null;
    }

    @Override
    public String visitReference(ReferenceTree node, Void unused) {
        return null;
    }

    @Override
    public String visitReturn(ReturnTree node, Void unused) {
        return null;
    }

    @Override
    public String visitSee(SeeTree node, Void unused) {
        return null;
    }

    @Override
    public String visitSerial(SerialTree node, Void unused) {
        return null;
    }

    @Override
    public String visitSerialData(SerialDataTree node, Void unused) {
        return null;
    }

    @Override
    public String visitSerialField(SerialFieldTree node, Void unused) {
        return null;
    }

    @Override
    public String visitSince(SinceTree node, Void unused) {
        return null;
    }

    @Override
    public String visitStartElement(StartElementTree node, Void unused) {
        return null;
    }

    @Override
    public String visitText(TextTree node, Void unused) {
        return node.getBody();
    }

    @Override
    public String visitThrows(ThrowsTree node, Void unused) {
        return null;
    }

    @Override
    public String visitUnknownBlockTag(UnknownBlockTagTree node, Void unused) {
        return null;
    }

    @Override
    public String visitUnknownInlineTag(UnknownInlineTagTree node, Void unused) {
        return null;
    }

    @Override
    public String visitValue(ValueTree node, Void unused) {
        return null;
    }

    @Override
    public String visitVersion(VersionTree node, Void unused) {
        return null;
    }

    @Override
    public String visitOther(DocTree node, Void unused) {
        return null;
    }
}
