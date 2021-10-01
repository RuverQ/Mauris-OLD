package com.ruverq.mauris.utils;

import com.ruverq.mauris.Mauris;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

// stealthily stole from Septicuss https://github.com/Septicuss
public class DataContainerUtil {

    public static String getData(Block block, String key) {
        if (isEmpty(block)) {
            clearData(block);
            return null;
        }

        final PersistentDataContainer blockContainer = getBlockPersistentDataContainer(block);
        final NamespacedKey namespacedKey = getNamespacedKey(block);

        if (blockContainer.isEmpty() || !blockContainer.has(namespacedKey, PersistentDataType.STRING)) {
            return null;
        }

        return blockContainer.get(namespacedKey, PersistentDataType.STRING);
    }

    public static void setData(Block block, String key, String value) {
        final PersistentDataContainer blockContainer = getBlockPersistentDataContainer(block);
        final NamespacedKey namespacedKey = getNamespacedKey(block);

        if (value == null) {
            blockContainer.remove(namespacedKey);
        } else {
            blockContainer.set(namespacedKey, PersistentDataType.STRING, value);
        }

        save(block, blockContainer);
    }

    private static void save(Block block, PersistentDataContainer blockContainer) {
        final PersistentDataContainer chunkContainer = block.getChunk().getPersistentDataContainer();
        final NamespacedKey key = getNamespacedKey(block);

        if (blockContainer.isEmpty()) {
            chunkContainer.remove(key);
        } else {
            chunkContainer.set(key, PersistentDataType.TAG_CONTAINER, blockContainer);
        }
    }

    private static PersistentDataContainer getBlockPersistentDataContainer(Block block) {
        final PersistentDataContainer chunkContainer = block.getChunk().getPersistentDataContainer();
        final PersistentDataContainer blockContainer;

        final NamespacedKey key = getNamespacedKey(block);

        if (chunkContainer.has(key, PersistentDataType.TAG_CONTAINER)) {
            blockContainer = chunkContainer.get(key, PersistentDataType.TAG_CONTAINER);
            assert blockContainer != null;
            return blockContainer;
        }

        blockContainer = chunkContainer.getAdapterContext().newPersistentDataContainer();
        chunkContainer.set(key, PersistentDataType.TAG_CONTAINER, blockContainer);

        return blockContainer;
    }

    public static void clearData(Block block) {
        final PersistentDataContainer blockContainer = getBlockPersistentDataContainer(block);
        blockContainer.getKeys().forEach(key -> blockContainer.remove(key));
        save(block, blockContainer);
    }

    public static boolean isEmpty(Block block) {
        if (block == null || block.getType().isAir()) {
            return true;
        }
        return false;
    }

    public static boolean hasData(Block block) {
        return !getBlockPersistentDataContainer(block).isEmpty();
    }

    public static boolean hasData(Block block, String key) {
        return getData(block, key) != null;
    }

    private static NamespacedKey getNamespacedKey(Block block) {
        final int x = block.getX() & 0x000F;
        final int y = block.getY() & 0x00FF;
        final int z = block.getZ() & 0x000F;
        return new NamespacedKey(Mauris.getInstance(), String.format("x%dy%dz%d", x, y, z));
    }

}
