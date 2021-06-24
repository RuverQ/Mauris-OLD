package com.ruverq.mauris.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class MaurisLootTable {

    @Setter
    @Getter
    List<MaurisLoot> loots = new ArrayList<>();

    public void addLoot(MaurisLoot loot){
        loots.add(loot);
    }

    public static MaurisLootTable fromConfigSection(ConfigurationSection section){
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
