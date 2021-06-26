package com.ruverq.mauris.items.listeners;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MaurisBlockCancel implements Listener {

    @EventHandler
    public void onNote(NotePlayEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){

        if(e.getAction() == Action.LEFT_CLICK_BLOCK) return;

        if(e.getClickedBlock() == null) return;
        BlockData bd = e.getClickedBlock().getBlockData();
        MaurisBlock mb = ItemsLoader.getMaurisBlock(bd);
        if(mb != null) e.setCancelled(true);
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent e){
        if(e.getChangedType() != Material.TRIPWIRE) return;
        e.setCancelled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendBlockChange(e.getSourceBlock().getLocation(), e.getSourceBlock().getBlockData());
                }
            }
        }.runTaskLater(Mauris.getInstance(), 1);
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

}
