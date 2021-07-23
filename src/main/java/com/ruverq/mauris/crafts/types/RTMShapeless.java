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
    public Recipe loadFromConfigurationSection(ConfigurationSection cs, ItemStack result, NamespacedKey namespace) {
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(namespace, result);

        for(String ingredient : cs.getStringList("ingredients")){

            MaurisItem mItem = ItemsLoader.getMaurisItem(ingredient);

            if(mItem == null){
                Material material = Material.matchMaterial(ingredient);
                if(material == null) continue;

                shapelessRecipe.addIngredient(material);
            }else{
                shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(mItem.getAsItemStack()));
            }

        }

        return shapelessRecipe;
    }

}
