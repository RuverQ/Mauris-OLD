package com.ruverq.mauris.crafts.types;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

public class RTMShapeless implements RecipeType{
    @Override
    public String name() {
        return "SHAPELESS";
    }

    @Override
    public Recipe loadFromConfigurationSection(RecipePreloadInformation rpi) {

        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(rpi.getKey(), rpi.getResult());

        for(String ingredient : rpi.getCraftCS().getStringList("ingredients")){

            MaurisItem mItem = ItemsLoader.getMaurisItem(ingredient);

            if(mItem == null){
                Material material = Material.matchMaterial(ingredient);
                if(material == null) continue;

                shapelessRecipe.addIngredient(material);
            }else{
                shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(mItem.getAsItemStack()));
            }
        }

        if(rpi.getGroup() != null && !rpi.getGroup().isEmpty()){
            shapelessRecipe.setGroup(rpi.getGroup());
        }

        return shapelessRecipe;
    }

}
