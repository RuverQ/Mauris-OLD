package com.ruverq.mauris.crafts.discover;

import com.ruverq.mauris.crafts.discover.listeners.QueueInformation;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface DiscoverListener extends Listener {

    String name();
    List<QueueInformation> queue = new ArrayList<>();

    default void addToQueue(String line, NamespacedKey key){
        QueueInformation qi = new QueueInformation(line, key);
        queue.add(qi);
    }

}
