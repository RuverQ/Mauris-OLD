package com.ruverq.mauris.crafts.discover.listeners;

import com.ruverq.mauris.crafts.discover.DiscoverListener;
import com.ruverq.mauris.items.ItemsLoader;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DLPlayerPickup implements DiscoverListener {
    @Override
    public String name() {
        return "PICKUP";
    }

    HashMap<ItemStack, List<NamespacedKey>> hashmap = new HashMap<>();

    @Override
    public void addToQueue(String line, NamespacedKey key) {
        queue.put(line, key);
        System.out.println(" loadas das");

        String[] args = line.split(" ");
        if(args.length < 2) return;

        for(String itemS : args){
            if(itemS.equalsIgnoreCase(name())) continue;

            ItemStack item = ItemsLoader.getMaurisItem(itemS, true);;

            List<NamespacedKey> namespacedKeys = hashmap.get(item);
            if(namespacedKeys == null) namespacedKeys = new ArrayList<>();
            namespacedKeys.add(key);

            hashmap.remove(item);
            hashmap.put(item, namespacedKeys);
        }

    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        Entity entity = e.getEntity();
        if(!(entity instanceof Player)) return;
        Player p = ((Player) entity);

        ItemStack item = e.getItem().getItemStack().clone();
        item.setAmount(1);

        List<NamespacedKey> list = hashmap.get(item);
        if(list == null) return;

        p.discoverRecipes(list);
    }
}
