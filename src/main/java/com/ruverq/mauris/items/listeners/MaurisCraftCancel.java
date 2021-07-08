package com.ruverq.mauris.items.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class MaurisCraftCancel implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e){
        for(ItemStack item : e.getInventory().getMatrix()){
            if(ItemsLoader.getMaurisItem(item) != null){
                e.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

}
