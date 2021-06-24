package com.ruverq.mauris.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MaurisLoot {

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    double chance;

    @Getter
    @Setter
    ItemStack itemStack;

    @Getter
    @Setter
    int max;

    @Getter
    @Setter
    int min;

    public void drop(Location location){
        World world = location.getWorld();
        if(world == null) return;

        world.dropItemNaturally(location, itemStack);
    }

    public void dropWithChance(Location location){
        if(!isWorked()) return;
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

    public static MaurisLoot fromConfigSection(ConfigurationSection section){
        MaurisLoot loot = new MaurisLoot();

        double chance = section.getDouble("chance", 0.5);

        String itemS = section.getString("item");
        ItemStack item = ItemsLoader.getMaurisItem(itemS, true);

        int min = section.getInt("min", 0);
        int max = section.getInt("max", 3);

        loot.setChance(chance);
        loot.setItemStack(item);
        loot.setMinNMaxValue(min,max);

        loot.setName(section.getName());

        return loot;
    }


}
