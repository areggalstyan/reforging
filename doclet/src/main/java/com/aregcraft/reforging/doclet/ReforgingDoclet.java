package com.aregcraft.reforging.doclet;

import com.aregcraft.reforging.doclet.model.Model;
import com.aregcraft.reforging.doclet.model.processor.ModelProcessor;
import com.aregcraft.reforging.doclet.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReforgingDoclet implements Doclet {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private Path rootDirectory;
    private Path resourcesDirectory;
    private Path modelsDirectory;
    private Path abilitiesDirectory;
    private Path externalDirectory;
    private String version;

    private static Map<String, Model> models(DocletEnvironment environment, String annotation) {
        return new ElementFinder(environment).byAnnotation(annotation).stream()
                .map(it -> ModelProcessor.create(environment, it))
                .map(ModelProcessor::nameModelPair)
                .collect(Collectors.toMap(Pair::key, Pair::value));
    }

    @Override
    public void init(Locale locale, Reporter reporter) {
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return Set.of(new SimpleOption(this::processDestinationDirectory, 1, "-d"),
                new SimpleOption(it -> version = it.get(0), 1, "-version"),
                new SimpleOption(null, 1, "-doctitle", "-windowtitle"),
                new SimpleOption(null, 0, "-notimestamp", "-quiet"));
    }

    private void processDestinationDirectory(List<String> arguments) {
        rootDirectory = Path.of(arguments.get(0));
        resourcesDirectory = rootDirectory.resolve("src/main/resources");
        modelsDirectory = rootDirectory.resolve("models");
        abilitiesDirectory = modelsDirectory.resolve("abilities");
        externalDirectory = modelsDirectory.resolve("external");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean run(DocletEnvironment environment) {
        var abilities = models(environment, "Ability");
        var external = models(environment, "External");
        abilities.forEach((key, value) -> SafeFiles.serialize(abilitiesDirectory.resolve(key), value, gson));
        external.forEach((key, value) -> SafeFiles.serialize(externalDirectory.resolve(key), value, gson));
        SafeFiles.serialize(modelsDirectory.resolve("manifest"), new Manifest(version, abilities.keySet(),
                external.keySet()), gson);
        TagReplacer.builder()
                .replacement("standard_reforges_json", resourcesDirectory.resolve("standard_reforges.json"))
                .replacement("ultimate_reforges_json", resourcesDirectory.resolve("ultimate_reforges.json"))
                .replacement("abilities_json", resourcesDirectory.resolve("abilities.json"))
                .replacement("abilities", new ModelFormat(abilities, external).format())
                .build().replace(rootDirectory.resolve("README.md"));
        return true;
    }
}
