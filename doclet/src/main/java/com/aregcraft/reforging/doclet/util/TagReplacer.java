package com.aregcraft.reforging.doclet.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TagReplacer {
    private static final String OPENING_TAG = "<!-- <%s> -->";
    private static final String CLOSING_TAG = "<!-- </%s> -->";

    private final Map<String, List<String>> openingTags;
    private final List<String> closingTags;

    public TagReplacer(Map<String, List<String>> replacements) {
        openingTags = replacements.entrySet().stream().collect(Collectors.toMap(it ->
                OPENING_TAG.formatted(it.getKey()), Map.Entry::getValue));
        closingTags = replacements.keySet().stream().map(CLOSING_TAG::formatted).toList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void replace(Path path) {
        var iterator = SafeFiles.readAllLines(path).iterator();
        var lines = new ArrayList<String>();
        var skip = false;
        while (iterator.hasNext()) {
            var line = iterator.next();
            if (closingTags.contains(line)) {
                skip = false;
                lines.add("");
            }
            if (!skip) {
                lines.add(line);
            }
            if (openingTags.containsKey(line)) {
                skip = true;
                lines.add("");
                lines.addAll(openingTags.get(line));
            }
        }
        SafeFiles.write(path, lines);
    }

    public static class Builder {
        private final Map<String, List<String>> replacements;

        private Builder() {
            replacements = new HashMap<>();
        }

        public Builder replacement(String tag, List<String> lines) {
            replacements.put(tag, lines);
            return this;
        }

        public Builder replacement(String tag, Path path) {
            var lines = SafeFiles.readAllLines(path);
            lines.add(0, "```json");
            lines.add("```");
            return replacement(tag, lines);
        }

        public TagReplacer build() {
            return new TagReplacer(replacements);
        }
    }
}
