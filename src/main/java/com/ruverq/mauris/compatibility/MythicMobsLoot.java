package com.ruverq.mauris.compatibility;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitItemStack;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicDropLoadEvent;
import io.lumine.xikage.mythicmobs.drops.droppables.ItemDrop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class MythicMobsLoot implements Listener {

    @EventHandler
    public void onLootLoad(MythicDropLoadEvent e){

        if(!e.getDropName().equalsIgnoreCase("mauris")) return;
        String line = e.getContainer().getLine();
        String[] args = line.split(" ");

        MaurisItem item = ItemsLoader.getMaurisItem(args[1]);
        MaurisItem item2 = ItemsLoader.getMaurisItem(args[2]);

        if(args.length == 4 && item != null){
            ItemStack itemstack = item.getAsItemStack();
            ItemDrop itemDrop = new ItemDrop(line, e.getConfig(), new BukkitItemStack(itemstack));
            e.register(itemDrop);
        }else if(args.length == 3 && item2 != null){
            ItemStack itemstack = item2.getAsItemStack();
            ItemDrop itemDrop = new ItemDrop(line, e.getConfig(), new BukkitItemStack(itemstack));
            e.register(itemDrop);
        }

    }

}
