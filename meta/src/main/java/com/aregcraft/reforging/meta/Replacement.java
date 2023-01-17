package com.aregcraft.reforging.meta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public enum Replacement {
    ABILITIES_JSON {
        @Override
        public String getValue(Path resourcesPath, List<Ability> abilities) throws IOException {
            return getJsonBlock(resourcesPath, "abilities");
        }
    },
    ITEM_JSON {
        @Override
        public String getValue(Path resourcesPath, List<Ability> abilities) throws IOException {
            return getJsonBlock(resourcesPath, "item");
        }
    },
    REFORGE_STONES_JSON {
        @Override
        public String getValue(Path resourcesPath, List<Ability> abilities) throws IOException {
            return getJsonBlock(resourcesPath, "reforge_stones");
        }
    },
    REFORGES_JSON {
        @Override
        public String getValue(Path resourcesPath, List<Ability> abilities) throws IOException {
            return getJsonBlock(resourcesPath, "reforges");
        }
    },
    REFORGING_ANVIL_JSON {
        @Override
        public String getValue(Path resourcesPath, List<Ability> abilities) throws IOException {
            return getJsonBlock(resourcesPath, "reforging_anvil");
        }
    },
    ABILITIES {
        @Override
        public String getValue(Path resourcesPath, List<Ability> abilities) {
            var builder = new StringBuilder("| Name | Description |\n")
                    .append("| --- | --- |");
            abilities.stream().map(this::getRow).forEach(builder::append);
            return builder.toString();
        }

        private String getRow(Ability ability) {
            return "\n| " + ability.name() + " | " + ability.description() + " |";
        }
    },
    BASES {
        @Override
        public String getValue(Path resourcesPath, List<Ability> abilities) {
            var builder = new StringBuilder();
            abilities.stream().map(this::getAbility).forEach(builder::append);
            builder.setLength(builder.length() - 2);
            return builder.toString();
        }

        private String getAbility(Ability ability) {
            var builder = new StringBuilder("#### ").append(ability.name()).append('\n')
                    .append('\n')
                    .append("| Name | Type | Description |\n")
                    .append("| --- | --- | --- |\n");
            ability.properties().stream().map(this::getRow).forEach(builder::append);
            return builder.append('\n').toString();
        }

        private String getRow(Property property) {
            return "| " + property.name() + " | `" + property.type() + "` | " + property.description() + " |\n";
        }
    };

    private final Pattern pattern;

    Replacement() {
        pattern = Pattern.compile(getTags("(?s).*"));
    }

    public static String replaceAll(String string, Path resourcesPath, List<Ability> abilities) {
        for (var value : values()) {
            string = value.replace(string, resourcesPath, abilities);
        }
        return string;
    }

    private static String getJsonBlock(Path resourcesPath, String name) throws IOException {
        return "```json\n" + Files.readString(resourcesPath.resolve(name + ".json")) + "\n```";
    }

    public String replace(String string, Path resourcesPath, List<Ability> abilities) {
        return pattern.matcher(string).replaceAll(it -> {
            try {
                return getTags('\n' + getValue(resourcesPath, abilities) + '\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getTags(String value) {
        return "<!-- <%s> -->%s<!-- </%1$s> -->".formatted(name().toLowerCase(), value);
    }

    public abstract String getValue(Path resourcesPath, List<Ability> abilities) throws IOException;
}
