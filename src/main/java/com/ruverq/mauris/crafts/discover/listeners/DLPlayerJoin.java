package com.ruverq.mauris.crafts.discover.listeners;

import com.ruverq.mauris.crafts.discover.DiscoverListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class DLPlayerJoin implements DiscoverListener {
    @Override
    public String name() {
        return "PLAYERJOIN";
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.discoverRecipes(queue.values());
    }

}
