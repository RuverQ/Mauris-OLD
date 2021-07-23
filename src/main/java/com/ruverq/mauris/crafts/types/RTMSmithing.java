package com.ruverq.mauris.crafts.types;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;

public class RTMSmithing implements RecipeType{
    @Override
    public String name() {
        return "SMITHING";
    }

    @Override
    public Recipe loadFromConfigurationSection(ConfigurationSection cs, ItemStack result, NamespacedKey namespace) {
        String fingrS = cs.getString("firstIngredient");
        String singrS = cs.getString("secondIngredient");

        if(fingrS == null || singrS == null) return null;

        Material first = Material.matchMaterial(fingrS);
        Material second = Material.matchMaterial(singrS);
        if(first == null || second == null) return null;

        SmithingRecipe smithingRecipe = new SmithingRecipe(namespace, result, new RecipeChoice.MaterialChoice(first), new RecipeChoice.MaterialChoice(second));
        return smithingRecipe;
    }

}
