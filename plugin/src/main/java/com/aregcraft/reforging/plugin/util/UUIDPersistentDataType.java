package com.aregcraft.reforging.plugin.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDPersistentDataType implements PersistentDataType<byte[], UUID> {
    public static final PersistentDataType<byte[], UUID> UUID = new UUIDPersistentDataType();

    private UUIDPersistentDataType() {
    }

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte[] toPrimitive(UUID complex, PersistentDataAdapterContext context) {
        return ByteBuffer.wrap(new byte[16])
                .putLong(complex.getMostSignificantBits())
                .putLong(complex.getLeastSignificantBits())
                .array();
    }

    @Override
    public UUID fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        var buffer = ByteBuffer.wrap(primitive);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
