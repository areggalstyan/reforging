package com.aregcraft.reforging;

import com.aregcraft.delta.api.json.annotation.JsonConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

@JsonConfiguration("update_checker")
public class UpdateChecker {
    private static final String META_URL = "https://raw.githubusercontent.com/Aregcraft/reforging/master/meta.json";

    private final String message;
    private final long period;

    public UpdateChecker(String message, long period) {
        this.message = message;
        this.period = period;
    }

    public void scheduleChecks(Reforging plugin) {
        plugin.getAsynchronousScheduler().scheduleRepeatingTask(() -> check(plugin), period);
    }

    public void check(Reforging plugin) {
        if (!plugin.getDescription().getVersion().equals(getLatestVersion(plugin))) {
            plugin.getLogger().log(Level.INFO, message);
        }
    }

    private String getLatestVersion(Reforging plugin) {
        try (var reader = new InputStreamReader(new URL(META_URL).openStream())) {
            return plugin.getGson().fromJson(reader, Meta.class).version;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Meta {
        private String version;
    }
}
