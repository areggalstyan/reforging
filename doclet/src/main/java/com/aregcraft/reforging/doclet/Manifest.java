package com.aregcraft.reforging.doclet;

import java.util.Set;

public record Manifest(String version, Set<String> abilities, Set<String> external) {
}
