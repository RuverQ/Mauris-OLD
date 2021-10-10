package com.ruverq.mauris.crafts.types;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class RTMShaped implements RecipeType{

    @Override
    public String name() {
        return "SHAPED";
    }

    @Override
    public Recipe loadFromConfigurationSection(RecipePreloadInformation rpi) {
        ConfigurationSection cs = rpi.getCraftCS();

        ShapedRecipe shapedRecipe = new ShapedRecipe(rpi.getKey(), rpi.getResult());

        String s1 = cs.getString("shape.1");
        String s2 = cs.getString("shape.2");
        String s3 = cs.getString("shape.3");

        if(s1 == null && s2 == null && s3 == null){
            return null;
        }
        else if(s2 == null && s3 == null){
            shapedRecipe.shape(s1);
        }else if(s3 == null){
            shapedRecipe.shape(s1, s2);
        }else{
            shapedRecipe.shape(s1, s2, s3);
        }

        if(cs.getConfigurationSection("ingredients") == null) return null;
        for(String charkey : cs.getConfigurationSection("ingredients").getKeys(false)){
            String materialS = cs.getString("ingredients." + charkey);
            if(materialS == null) continue;

            String[] choices = materialS.split(" ");

            ChoiceUnit unit = new ChoiceUnit();
            unit.addItems(choices);
            
            shapedRecipe.setIngredient(charkey.toCharArray()[0], unit.buildChoice());
        }

        if(rpi.getGroup() != null && !rpi.getGroup().isEmpty()){
            shapedRecipe.setGroup(rpi.getGroup());
        }

        return shapedRecipe;
    }

}
