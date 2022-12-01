package com.aregcraft.reforgingdoclet.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class SafeFiles {
    public static void read(Path path, Consumer<BufferedReader> action) {
        try (var reader = Files.newBufferedReader(path)) {
            action.accept(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(Path path, Consumer<BufferedWriter> action) {
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (var writer = Files.newBufferedWriter(path)) {
            action.accept(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(Path path, Iterable<? extends String> lines) {
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void serialize(Path path, Object obj, Gson gson) {
        write(path.resolveSibling(path.getFileName().toString() + ".json"), it -> gson.toJson(obj, it));
    }

    public static List<String> readAllLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
