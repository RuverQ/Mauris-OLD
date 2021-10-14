package com.ruverq.mauris.crafts;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MaurisRecipe {

    @Getter
    @Setter
    File file;

    @Getter
    @Setter
    String discoverBy;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    Recipe recipe;

    @Getter
    @Setter
    List<ItemStack> returnItems = new ArrayList<>();

    public void addItems(List<String> itemNames){
        itemNames.forEach(this::addItem);
    }

    public void addItem(String name) {
        MaurisItem mItem = ItemsLoader.getMaurisItem(name);

        if(mItem == null){
            Material material = Material.matchMaterial(name);
            if(material == null) return;

            returnItems.add(new ItemStack(material));
        }else{
            returnItems.add(mItem.getAsItemStack());
        }
    }

}
