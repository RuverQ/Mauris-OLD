package com.ruverq.mauris.items.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemPlantCancel implements Listener {

    @EventHandler
    public void onPlant(PlayerInteractEvent e){
        Action action = e.getAction();
        if(action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = e.getItem();
        if(item == null) return;

        Block b = e.getClickedBlock();
        if(b.getType() != Material.FARMLAND) return;

        MaurisItem mitem = ItemsLoader.getMaurisItem(item);
        if(mitem != null) e.setCancelled(true);
    }

}
