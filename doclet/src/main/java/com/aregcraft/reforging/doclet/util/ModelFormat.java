package com.aregcraft.reforging.doclet.util;

import com.aregcraft.reforging.doclet.model.Model;
import com.aregcraft.reforging.doclet.model.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ModelFormat(Map<String, Model> abilities, Map<String, Model> external) {
    private static String toLowerCase(String text) {
        return String.join(" ", text.split("(?=[A-Z])")).toLowerCase();
    }

    public List<String> format() {
        var lines = new ArrayList<String>();
        abilities.forEach((key, value) -> {
            if (lines.size() > 0) {
                lines.add("");
            }
            formatModel(key, value, lines);
        });
        return lines;
    }

    private void formatModel(String name, Model model, List<String> lines) {
        lines.add("### " + name);
        lines.add("");
        lines.add(model.description());
        model.external().forEach((key, value) -> {
            var externalModel = external.get(value);
            lines.add("");
            lines.add("#### " + key);
            lines.add("");
            lines.add(externalModel.description());
            formatProperties(externalModel.properties(), lines, true);
        });
        formatProperties(model.properties(), lines, false);
    }

    private void formatProperties(Map<String, Property> properties, List<String> lines, boolean external) {
        properties.forEach((key, value) -> {
            lines.add("");
            lines.add("#".repeat(external ? 5 : 4) + " " + key + ": " + toLowerCase(value.type()));
            lines.add("");
            lines.add(value.description());
        });
    }
}
