package com.aregcraft.reforging.meta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ReforgingMeta implements Doclet {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private Path metaPath;
    private Path descriptionPath;
    private Path resourcesPath;
    private String version;

    @Override
    public void init(Locale locale, Reporter reporter) {
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return Set.of(StandardOption.oneArg(this::setupPaths, "-d"),
                StandardOption.oneArg(it -> version = it, "-version"),
                StandardOption.oneArg("-doctitle", "-windowtitle"),
                StandardOption.noArgs("-notimestamp"));
    }

    private void setupPaths(String arg) {
        var path = Path.of(arg);
        metaPath = path.resolve("meta.json");
        descriptionPath = path.resolve("README.md");
        resourcesPath = path.resolve("src/main/resources");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean run(DocletEnvironment environment) {
        try (var writer = Files.newBufferedWriter(metaPath)) {
            var abilities = getAbilities(environment);
            gson.toJson(new Meta(version, abilities), writer);
            Files.writeString(descriptionPath,
                    Replacement.replaceAll(Files.readString(descriptionPath), resourcesPath, abilities));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private List<Ability> getAbilities(DocletEnvironment environment) {
        return environment.getSpecifiedElements().stream()
                .filter(it -> it.getAnnotation(ProcessedAbility.class) != null)
                .map(TypeElement.class::cast)
                .map(new AbilityProcessor(environment)::process)
                .toList();
    }
}
