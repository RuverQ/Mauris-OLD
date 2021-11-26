package com.ruverq.mauris.items.blocks.listeners.paper;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.blocks.listeners.MaurisBlockBreak;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class PaperMaurisBlockBreak implements Listener {

    @EventHandler
    public void onBreak(BlockDestroyEvent e){
        if(!e.willDrop()) return;

        Block block = e.getBlock();
        BlockData blockNewState = e.getNewState();

        if(block.getType() != Material.WEEPING_VINES_PLANT && block.getType() != Material.WEEPING_VINES) return;
        boolean dropped = MaurisBlockBreak.drop(block.getType(), block.getLocation(), new ItemStack(Material.AIR));
        if(dropped){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Collection<Entity> items = block.getLocation().getWorld().getNearbyEntities(block.getLocation().add(0.5,0.5,0.5), 2, 2, 2, (en) -> en.getType() == EntityType.DROPPED_ITEM);
                    for(Entity entity : items){
                        Item item = (Item) entity;
                        if(item.getItemStack().isSimilar(new ItemStack(Material.WEEPING_VINES))) item.remove();
                    }
                }
            }.runTaskLater(Mauris.getInstance(), 1);
        }

    }

}
