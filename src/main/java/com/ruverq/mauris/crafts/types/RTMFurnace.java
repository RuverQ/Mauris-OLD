package com.ruverq.mauris.crafts.types;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

public class RTMFurnace implements RecipeType{
    @Override
    public String name() {
        return "FURNACE";
    }

    @Override
    public Recipe loadFromConfigurationSection(ConfigurationSection cs, ItemStack result, NamespacedKey namespace) {
        float experience = (float) cs.getDouble("experience", 20);
        int cookingTime = cs.getInt("cookingTime", 20);
        String ingrS = cs.getString("ingredient");
        if(ingrS == null) return null;

        MaurisItem mItem = ItemsLoader.getMaurisItem(ingrS);
        if(mItem == null){
            Material ingr = Material.matchMaterial(ingrS);
            if(ingr == null) return null;

            return new FurnaceRecipe(namespace, result, ingr, experience, cookingTime);
        }else{
            return new FurnaceRecipe(namespace, result, new RecipeChoice.ExactChoice(mItem.getAsItemStack()), experience, cookingTime);
        }
    }

}
