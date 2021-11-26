package com.ruverq.mauris.crafts.types;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;

public class RTMSmoker implements RecipeType{
    @Override
    public String name() {
        return "SMOKER";
    }

    @Override
    public Recipe loadFromConfigurationSection(RecipePreloadInformation rpi) {
        ConfigurationSection cs = rpi.getCraftCS();

        float experience = (float) cs.getDouble("experience", 20);
        int cookingTime = cs.getInt("cookingTime", 20);
        String ingrS = cs.getString("ingredient");
        if(ingrS == null) return null;

        ChoiceUnit cu = new ChoiceUnit(rpi.isOnlyItemStacks());
        String[] splitted = ingrS.split(" ");
        cu.addItems(splitted);

        RecipeChoice choice = cu.buildChoice();
        if(choice == null) {
            return null;
        }

        SmokingRecipe recipe = new SmokingRecipe(rpi.getKey(), rpi.getResult(), choice, experience, cookingTime);

        if(rpi.getGroup() != null && !rpi.getGroup().isEmpty()){
            recipe.setGroup(rpi.getGroup());
        }

        return recipe;
    }

}
