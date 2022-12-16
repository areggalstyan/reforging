package com.aregcraft.reforging.core.data;

import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PersistentDataTypeExtension {
    public static final PersistentDataType<byte[], UUID> UUID = new UUIDPersistentDataType();
    public static final PersistentDataType<Byte, Boolean> BOOLEAN = new BooleanPersistentDataType();
}
