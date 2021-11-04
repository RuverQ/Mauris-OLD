package com.ruverq.mauris.crafts.types;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.ArrayList;
import java.util.List;

public class ChoiceUnit {

    List<Material> materialList = new ArrayList<>();
    List<ItemStack> itemStacks = new ArrayList<>();

    public ChoiceUnit() {

    }

    public ChoiceUnit(boolean onlyItemStacks) {
        this.onlyItemStacks = onlyItemStacks;
    }

    boolean onlyItemStacks;

    public void addItem(Material material) {
        materialList.add(material);
    }

    public void addItem(ItemStack itemStack) {
        itemStacks.add(itemStack);
    }

    public void addItem(String name) {
        MaurisItem mItem = ItemsLoader.getMaurisItem(name);

        if(mItem == null){
            Material material = Material.matchMaterial(name);
            if(material == null) {
                Bukkit.getLogger().warning("Can't find " + name + " for the ingredient");
                return;
            }

            if(onlyItemStacks) addItem(new ItemStack(material));
            else addItem(material);
        }else{
            addItem(mItem.getAsItemStack());
        }
    }

    public void addItems(String[] choices){
        for(String choice : choices){
            addItem(choice);
        }
    }

    public RecipeChoice buildChoice(){

        if(itemStacks.isEmpty() && !materialList.isEmpty()){
            return new RecipeChoice.MaterialChoice(materialList);
        }

        List<ItemStack> allIngredients = new ArrayList<>();
        allIngredients.addAll(itemStacks);
        for(Material material : materialList){
            allIngredients.add(new ItemStack(material));
        }

        return new RecipeChoice.ExactChoice(itemStacks);
    }


}
