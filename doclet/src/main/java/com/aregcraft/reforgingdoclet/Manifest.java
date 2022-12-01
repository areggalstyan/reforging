package com.aregcraft.reforgingdoclet;

import java.util.Set;

public record Manifest(String version, Set<String> abilities, Set<String> external) {
}
