package com.ruverq.mauris.crafts.discover;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public interface DiscoverListener extends Listener {

    String name();
    List<NamespacedKey> queue = new ArrayList<>();

    default void addToQueue(NamespacedKey key){
        queue.add(key);
    }

}
