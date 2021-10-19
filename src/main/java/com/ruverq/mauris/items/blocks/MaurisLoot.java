package com.ruverq.mauris.items.blocks;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MaurisLoot {

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    double chance;

    @Getter
    @Setter
    String item;

    @Getter
    @Setter
    int max;

    @Getter
    @Setter
    int amount;

    @Getter
    @Setter
    List<String> allowTools = new ArrayList<>();

    @Getter
    @Setter
    List<String> blockTools = new ArrayList<>();

    @Getter
    @Setter
    int min;

    public ItemStack loot(){
        int amount;
        if(min < 0 || max < 0){
            amount = getAmount();
        }else{
            amount = ThreadLocalRandom.current().nextInt(min, max);
        }

        ItemStack item = getItemStack().clone();
        item.setAmount(amount);

        return item;
    }

    public ItemStack randomLoot(double luck){
        if(!isWorked(luck)) return null;
        int amount;
        if(min < 0 || max < 0){
            amount = getAmount();
        }else{
            amount = ThreadLocalRandom.current().nextInt(min, max);
        }

        ItemStack item = getItemStack().clone();
        item.setAmount(amount);

        return item;
    }

    public void drop(Location location){
        World world = location.getWorld();
        if(world == null) return;

        world.dropItemNaturally(location, loot());
    }

    public void dropWithChance(Location location, ItemStack tool, double luck){
        if(!allowTools.isEmpty()){
            if(tool == null) return;
            MaurisItem item = ItemsLoader.getMaurisItem(tool);
            if(item == null){

                boolean allow = false;
                for(String toolS : allowTools){
                    if(toolS.equalsIgnoreCase(tool.getType().name())){
                        allow = true;
                        break;
                    }
                }
                if(!allow) return;

            }else{
                if(!allowTools.contains(item.getName())) return;
            }
        }

        if(!blockTools.isEmpty()){
            if(tool == null) return;
            MaurisItem item = ItemsLoader.getMaurisItem(tool);
            if(item == null){
                for(String toolS : allowTools){
                    if(toolS.equalsIgnoreCase(tool.getType().name())) return;
                }
            }else{
                if(blockTools.contains(item.getName())) return;
            }
        }

        if(!isWorked(luck)) return;
        drop(location);
    }

    public boolean isWorked(double luck){
        return new Random().nextDouble() <= chance * luck;
    }

    public boolean isWorked(){
        return isWorked(1);
    }

    public void setMinNMaxValue(int min, int max){
        this.min = min;
        this.max = max;
    }

    public ItemStack getItemStack(){
        return ItemsLoader.getMaurisItem(item, true);
    }

    public static MaurisLoot fromConfigSection(ConfigurationSection section){
        MaurisLoot loot = new MaurisLoot();

        double chance = section.getDouble("chance", 1);

        String itemS = section.getString("item");

        int min = section.getInt("min", -1);
        int max = section.getInt("max", -1);

        int amount = section.getInt("amount", 1);

        List<String> allow_tools = section.getStringList("allow-tools");
        List<String> block_tools = section.getStringList("block-tools");

        loot.setChance(chance);
        loot.setItem(itemS);
        loot.setMinNMaxValue(min,max);
        loot.setAllowTools(allow_tools);
        loot.setBlockTools(block_tools);
        loot.setAmount(amount);

        loot.setName(section.getName());

        return loot;
    }


}
