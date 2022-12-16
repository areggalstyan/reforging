package com.aregcraft.reforging.core.data;

import com.aregcraft.reforging.core.Context;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class Data {
    private final PersistentDataContainer dataContainer;

    public static Data of(PersistentDataHolder dataHolder) {
        return new Data(dataHolder.getPersistentDataContainer());
    }

    private Data(PersistentDataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public <T, Z> Z get(String key, PersistentDataType<T, Z> type) {
        return dataContainer.get(Context.plugin().key(key), type);
    }

    public <T, Z> Z getOrElse(String key, PersistentDataType<T, Z> type, Z other) {
        return Optional.ofNullable(dataContainer.get(Context.plugin().key(key), type)).orElse(other);
    }

    public <T, Z> void set(String key, PersistentDataType<T, Z> type, Z value) {
        dataContainer.set(Context.plugin().key(key), type, value);
    }

    public <T, Z> boolean has(String key, PersistentDataType<T, Z> type) {
        return dataContainer.has(Context.plugin().key(key), type);
    }
}
