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
    public Recipe loadFromConfigurationSection(RecipePreloadInformation rpi) {
        ConfigurationSection cs = rpi.getCraftCS();
        float experience = (float) cs.getDouble("experience", 20);
        int cookingTime = cs.getInt("cookingTime", 20);
        String ingrS = cs.getString("ingredient");
        if(ingrS == null) return null;

        ChoiceUnit cu = new ChoiceUnit();
        String[] splitted = ingrS.split(" ");
        cu.addItems(splitted);

        FurnaceRecipe recipe = new FurnaceRecipe(rpi.getKey(), rpi.getResult(), cu.buildChoice(), experience, cookingTime);

        if(rpi.getGroup() != null && !rpi.getGroup().isEmpty()){
            recipe.setGroup(rpi.getGroup());
        }

        return recipe;
    }

}
