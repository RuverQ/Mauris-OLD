package com.ruverq.mauris.crafts.listeners;

import com.ruverq.mauris.crafts.CraftingManager;
import com.ruverq.mauris.crafts.MaurisRecipe;
import com.ruverq.mauris.crafts.events.MaurisCraftEvent;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Recipe;

public class CraftListener implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e){
        Recipe recipe = e.getRecipe();

        for(MaurisRecipe mRecipe : CraftingManager.getLoadedRecipes()){
            if(mRecipe.getRecipe() == recipe){
                MaurisCraftEvent mce = new MaurisCraftEvent(e.getRecipe(), e.getView(), e.getSlotType(), e.getSlot(), e.getClick(), e.getAction(), mRecipe);
                Bukkit.getPluginManager().callEvent(mce);
                if(mce.isCancelled()) e.setCancelled(true);
                break;
            }
        }


    }

}
