package com.ruverq.mauris.crafts.listeners;

import com.ruverq.mauris.crafts.CraftingManager;
import com.ruverq.mauris.crafts.MaurisRecipe;
import com.ruverq.mauris.crafts.events.MaurisCraftEvent;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Arrays;

public class CraftListener implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e){
        Recipe recipe = e.getRecipe();

        if(!(recipe instanceof Keyed)) return;
        Keyed keyed = (Keyed) recipe;
        String fullkey = keyed.getKey().getNamespace() + ":" + keyed.getKey().getKey();
        if(!CraftingManager.getNamespaceKeysOfRecipes().contains(fullkey)) return;

        String name = keyed.getKey().getKey();
        MaurisRecipe mRecipe = CraftingManager.getRecipeByName(name);

        MaurisCraftEvent mce = new MaurisCraftEvent(e.getRecipe(), e.getView(), e.getSlotType(), e.getSlot(), e.getClick(), e.getAction(), mRecipe);
        Bukkit.getPluginManager().callEvent(mce);
        if(mce.isCancelled()) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = false)
    public void onMaurisCraft(MaurisCraftEvent e){
        MaurisRecipe mr = e.getMaurisRecipe();

        Player p = (Player) e.getWhoClicked();

        mr.getReturnItems().forEach((i) -> addItemNaturally(p, i));

        p.updateInventory();
    }

    public void addItemNaturally(Player p, ItemStack item){
        if(canPutItem(p.getInventory(), item)){
            p.getInventory().addItem(item);
        }else{
            p.getLocation().getWorld().dropItem(p.getLocation(), item);
        }
    }

    public boolean canPutItem(Inventory inventory, ItemStack item){
        boolean hasAvailableSlot = Arrays.stream(inventory.getStorageContents()).toList().contains(null);
        if(hasAvailableSlot) return true;

        for(ItemStack storageItem : inventory.getStorageContents()){
            if(storageItem.isSimilar(item)){
                if(storageItem.getAmount() + item.getAmount() <= 64) return true;
            }
        }
        return false;
    }

}
