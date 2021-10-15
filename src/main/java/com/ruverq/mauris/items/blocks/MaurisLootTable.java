package com.ruverq.mauris.items.blocks;

import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.Mauris;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MaurisLootTable {

    @Setter
    @Getter
    List<MaurisLoot> loots = new ArrayList<>();

    public void addLoot(MaurisLoot loot){
        loots.add(loot);
    }

    public void dropAll(Location location, ItemStack tool, double luck){
        for(MaurisLoot maurisLoot : loots){
            maurisLoot.dropWithChance(location, tool, luck);
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

    public void dropAll(Location location, ItemStack tool){
        dropAll(location, tool, 1);
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

    public static MaurisLootTable getLootTableByName(String name){
        name = name.toLowerCase();
        return maurisLootHashMap.get(name);
    }

    static HashMap<String, MaurisLootTable> maurisLootHashMap = new HashMap<>();
    public static void loadLootTables(){
        maurisLootHashMap.clear();

        File folder = new File(Mauris.getInstance().getDataFolder() + File.separator + "loottables");
        folder.mkdirs();

        File[] files = folder.listFiles();
        if(files == null) return;
        for(File file : files){
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

            ConfigurationSection sec = yml.getConfigurationSection("");
            for(String name : sec.getKeys(false)){
                MaurisLootTable table = MaurisLootTable.fromConfigSection(sec.getConfigurationSection(name));
                if(table == null) continue;

                maurisLootHashMap.put(name.toLowerCase(), table);
            }
        }

    }

}
