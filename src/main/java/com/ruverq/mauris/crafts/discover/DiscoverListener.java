package com.ruverq.mauris.crafts.discover;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import java.util.HashMap;

public interface DiscoverListener extends Listener {

    String name();
    HashMap<String, NamespacedKey> queue = new HashMap<>();

    default void addToQueue(String line, NamespacedKey key){
        queue.put(line, key);
    }

}
