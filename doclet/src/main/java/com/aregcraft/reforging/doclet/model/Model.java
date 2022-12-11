package com.aregcraft.reforging.doclet.model;

import java.util.HashMap;
import java.util.Map;

public record Model(String description, Map<String, Property> properties, Map<String, String> external) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String description;
        private final Map<String, Property> properties;
        private final Map<String, String> external;

        private Builder() {
            properties = new HashMap<>();
            external = new HashMap<>();
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder property(String name, String type, String description) {
            properties.put(name, new Property(type, description));
            return this;
        }

        public Builder external(String name, String type) {
            external.put(name, type);
            return this;
        }

        public Model build() {
            return new Model(description, properties, external);
        }
    }
}
