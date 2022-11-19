package com.aregcraft.reforging.format;

import net.md_5.bungee.api.ChatColor;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ChatText {
    public static final Pattern COLORS = Pattern.compile("%(#[a-fA-F0-9]{6}|[a-zA-Z_]+)%");

    public static String colorize(String text) {
        return COLORS.matcher(text).replaceAll(it -> {
            try {
                return ChatColor.of(it.group(1)).toString();
            } catch (IllegalArgumentException e) {
                return it.group();
            }
        });
    }

    public static String format(String text, Map<String, Object> map) {
        return format(text, map, Function.identity());
    }

    public static String format(String text, Map<String, Object> map, Function<Object, Object> mapper) {
        for (var key : map.keySet()) {
            text = text.replaceAll(key, String.valueOf(mapper.apply(map.get(key))));
        }
        return colorize(text);
    }
}
