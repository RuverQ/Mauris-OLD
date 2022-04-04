package com.ruverq.mauris.utils;

import com.ruverq.mauris.Mauris;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityDataContainerUtil {

    public static String getData(Entity entity, String key) {

        final PersistentDataContainer blockContainer = entity.getPersistentDataContainer();
        final NamespacedKey namespacedKey = getNamespacedKey(key);

        if (blockContainer.isEmpty() || !blockContainer.has(namespacedKey, PersistentDataType.STRING)) {
            return null;
        }

        return blockContainer.get(namespacedKey, PersistentDataType.STRING);
    }

    public static void setData(Entity entity, String key, String value) {
        final PersistentDataContainer blockContainer = entity.getPersistentDataContainer();
        final NamespacedKey namespacedKey = getNamespacedKey(key);

        if (value == null) {
            blockContainer.remove(namespacedKey);
        } else {
            blockContainer.set(namespacedKey, PersistentDataType.STRING, value);
        }

        save(entity, blockContainer);
    }

    private static final String defaultName = "info";
    private static void save(Entity entity, PersistentDataContainer entityContainer) {
        final PersistentDataContainer container = entity.getPersistentDataContainer();
        final NamespacedKey key = getNamespacedKey(defaultName);

        if (entity.isEmpty()) {
            container.remove(key);
        } else {
            container.set(key, PersistentDataType.TAG_CONTAINER, entityContainer);
        }
    }

    public static void clearData(Entity entity) {
        final PersistentDataContainer container = entity.getPersistentDataContainer();
        container.getKeys().forEach(container::remove);
        save(entity, container);
    }

    public static boolean hasData(Entity entity) {
        return !entity.getPersistentDataContainer().isEmpty();
    }

    public static boolean hasData(Entity entity, String key) {
        return getData(entity, key) != null;
    }

    private static NamespacedKey getNamespacedKey(String name) {
        return new NamespacedKey(Mauris.getInstance(), name);
    }

}
