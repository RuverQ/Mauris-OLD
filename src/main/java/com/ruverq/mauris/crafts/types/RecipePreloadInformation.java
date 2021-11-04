package com.ruverq.mauris.crafts.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@AllArgsConstructor
public class RecipePreloadInformation {

    ItemStack result;
    ConfigurationSection craftCS;
    NamespacedKey key;
    String group;
    boolean onlyItemStacks;

}
