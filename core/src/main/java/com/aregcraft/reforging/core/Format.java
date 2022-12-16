package com.aregcraft.reforging.core;

import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

public record Format(Map<String, Object> context, Map<Class<?>, Function<Object, Object>> mappers) {
    public static final Format DEFAULT = Format.builder().build();
    private static final Pattern COLORS = Pattern.compile("%(#[a-fA-F0-9]{6}|[a-zA-Z_]+)%");

    public static Builder builder() {
        return new Builder();
    }

    public String format(String text) {
        for (var key : context.keySet()) {
            text = text.replaceAll("%" + key + "%", String.valueOf(map(context.get(key))));
        }
        return COLORS.matcher(text).replaceAll(it -> {
            try {
                return ChatColor.of(it.group(1)).toString();
            } catch (IllegalArgumentException e) {
                return it.group();
            }
        });
    }

    private Object map(Object value) {
        return Optional.ofNullable(mappers.get(value.getClass())).orElseGet(Function::identity).apply(value);
    }

    public static class Builder {
        private final Map<String, Object> context;
        private final Map<Class<?>, Function<Object, Object>> mappers;

        private Builder() {
            context = new HashMap<>();
            mappers = new HashMap<>();
        }

        public Builder entry(String key, Object value) {
            context.put(key, value);
            return this;
        }

        public Builder mapper(Class<?> type, Function<Object, Object> mapper) {
            mappers.put(type, mapper);
            return this;
        }

        public Format build() {
            return new Format(context, mappers);
        }
    }
}
