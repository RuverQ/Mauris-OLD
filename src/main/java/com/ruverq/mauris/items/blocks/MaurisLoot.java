package com.ruverq.mauris.items.blocks;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class MaurisLoot {

    String name;

    double chance;

    String item;

    int max;

    int amount;

    List<String> allowTools = new ArrayList<>();

    List<String> blockTools = new ArrayList<>();

    List<String> enchantmentList = new ArrayList<>();

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

    public boolean dropWithChance(Location location, ItemStack tool, double luck){
        if(!allowTools.isEmpty()){
            if(tool == null) return false;
            MaurisItem item = ItemsLoader.getMaurisItem(tool);
            if(item == null){

                boolean allow = false;
                for(String toolS : allowTools){
                    if(toolS.equalsIgnoreCase(tool.getType().name())){
                        allow = true;
                        break;
                    }
                }
                if(!allow) return false;

            }else{
                if(!allowTools.contains(item.getName())) return false;
            }
        }

        if(!blockTools.isEmpty()){
            if(tool == null) return false;
            MaurisItem item = ItemsLoader.getMaurisItem(tool);
            if(item == null){
                for(String toolS : allowTools){
                    if(toolS.equalsIgnoreCase(tool.getType().name())) return false;
                }
            }else{
                if(blockTools.contains(item.getName())) return false;
            }
        }

        if(!enchantmentList.isEmpty()){
            if(tool == null) return false;
            Set<Enchantment> toolEnchantments = tool.getEnchantments().keySet();
            Set<Enchantment> ourEnchantments = new HashSet<>();
            for(String enchS : enchantmentList){
                ourEnchantments.add(Enchantment.getByName(enchS.toUpperCase()));
            }
            if(!toolEnchantments.containsAll(ourEnchantments)) return false;
        }

        if(!isWorked(luck)) return false;
        drop(location);
        return true;
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

        List<String> enchantments = section.getStringList("enchantments");

        List<String> allow_tools = section.getStringList("allow-tools");
        List<String> block_tools = section.getStringList("block-tools");

        loot.setChance(chance);
        loot.setItem(itemS);
        loot.setMinNMaxValue(min,max);
        loot.setAllowTools(allow_tools);
        loot.setBlockTools(block_tools);
        loot.setAmount(amount);
        loot.setEnchantmentList(enchantments);

        loot.setName(section.getName());

        return loot;
    }


}
