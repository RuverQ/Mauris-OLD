package com.ruverq.mauris.crafts.types;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
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

        MaurisItem mItem = ItemsLoader.getMaurisItem(ingrS);

        SmokingRecipe recipe = null;
        if(mItem == null){
            Material ingr = Material.matchMaterial(ingrS);
            if(ingr == null) return null;

            recipe = new SmokingRecipe(rpi.getKey(), rpi.getResult(), ingr, experience, cookingTime);
        }else{
            recipe = new SmokingRecipe(rpi.getKey(), rpi.getResult(), new RecipeChoice.ExactChoice(mItem.getAsItemStack()), experience, cookingTime);
        }

        if(rpi.getGroup() != null && !rpi.getGroup().isEmpty()){
            recipe.setGroup(rpi.getGroup());
        }

        return recipe;
    }

}
