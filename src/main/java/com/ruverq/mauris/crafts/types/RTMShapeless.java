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

import java.util.ArrayList;
import java.util.List;

public class RTMShapeless implements RecipeType{
    @Override
    public String name() {
        return "SHAPELESS";
    }

    @Override
    public Recipe loadFromConfigurationSection(RecipePreloadInformation rpi) {

        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(rpi.getKey(), rpi.getResult());

        for(String ingredient : rpi.getCraftCS().getStringList("ingredients")){

            String[] choices = ingredient.split(" ");

            ChoiceUnit cu = new ChoiceUnit(rpi.isOnlyItemStacks());
            cu.addItems(choices);

            shapelessRecipe.addIngredient(cu.buildChoice());
        }

        if(rpi.getGroup() != null && !rpi.getGroup().isEmpty()){
            shapelessRecipe.setGroup(rpi.getGroup());
        }

        return shapelessRecipe;
    }

}
