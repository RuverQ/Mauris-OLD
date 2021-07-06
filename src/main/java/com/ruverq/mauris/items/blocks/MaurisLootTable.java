package com.ruverq.mauris.items.blocks;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MaurisLootTable {

    @Setter
    @Getter
    List<MaurisLoot> loots = new ArrayList<>();

    public void addLoot(MaurisLoot loot){
        loots.add(loot);
    }

    public void dropAll(Location location, double luck){
        for(MaurisLoot maurisLoot : loots){
            maurisLoot.dropWithChance(location);
        }
    }

    public List<ItemStack> randomLoot(){
        List<ItemStack> loot = new ArrayList<>();

        for(MaurisLoot maurisLoot : loots){
            ItemStack item = maurisLoot.randomLoot(1);
            if(item == null) continue;

            loot.add(item);
        }

        return loot;
    }

    public List<ItemStack> randomLoot(double luck){
        List<ItemStack> loot = new ArrayList<>();

        for(MaurisLoot maurisLoot : loots){
            ItemStack item = maurisLoot.randomLoot(luck);
            if(item == null) continue;

            loot.add(item);
        }

        return loot;
    }

    public void dropAll(Location location){
        dropAll(location, 1);
    }

    public static MaurisLootTable fromConfigSection(ConfigurationSection section){
        if(section == null) return null;

        MaurisLootTable table = new MaurisLootTable();

        for(String lootS : section.getKeys(false)){
            ConfigurationSection lootSection = section.getConfigurationSection(lootS);
            if(lootSection == null) continue;

            MaurisLoot maurisLoot = MaurisLoot.fromConfigSection(lootSection);
            table.addLoot(maurisLoot);
        }

        return table;
    }

}
