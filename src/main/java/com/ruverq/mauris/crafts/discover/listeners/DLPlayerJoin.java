package com.ruverq.mauris.crafts.discover.listeners;

import com.ruverq.mauris.crafts.discover.DiscoverListener;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class DLPlayerJoin implements DiscoverListener {
    @Override
    public String name() {
        return "PLAYERJOIN";
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        List<NamespacedKey> keys = new ArrayList<>();
        queue.forEach((qi) -> keys.add(qi.getKey()));

        p.discoverRecipes(keys);
    }

}
