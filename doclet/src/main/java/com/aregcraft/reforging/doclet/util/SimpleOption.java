package com.aregcraft.reforging.doclet.util;

import jdk.javadoc.doclet.Doclet;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record SimpleOption(Consumer<List<String>> action, int argumentCount, String... names) implements Doclet.Option {
    @Override
    public int getArgumentCount() {
        return argumentCount;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Kind getKind() {
        return Kind.STANDARD;
    }

    @Override
    public List<String> getNames() {
        return List.of(names);
    }

    @Override
    public String getParameters() {
        return "";
    }

    @Override
    public boolean process(String option, List<String> arguments) {
        Optional.ofNullable(action).ifPresent(it -> it.accept(arguments));
        return true;
    }
}
