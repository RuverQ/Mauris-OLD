package com.ruverq.mauris.crafts.types;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface RecipeType {

    String name();

    Recipe loadFromConfigurationSection(ConfigurationSection cs, ItemStack result, NamespacedKey namespace);

}
