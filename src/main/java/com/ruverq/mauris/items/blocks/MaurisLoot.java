package com.ruverq.mauris.items.blocks;

import com.ruverq.mauris.items.ItemsLoader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

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
    int min;

    public ItemStack loot(){
        int randomAmount = ThreadLocalRandom.current().nextInt(min, max);

        ItemStack item = getItemStack().clone();
        item.setAmount(randomAmount);

        return item;
    }

    public ItemStack randomLoot(double luck){
        if(!isWorked(luck)) return null;
        int randomAmount = ThreadLocalRandom.current().nextInt(min, max);

        ItemStack item = getItemStack().clone();
        item.setAmount(randomAmount);

        return item;
    }

    public void drop(Location location){
        World world = location.getWorld();
        if(world == null) return;

        world.dropItemNaturally(location, loot());
    }

    public void dropWithChance(Location location, double luck){
        if(!isWorked(luck)) return;
        drop(location);
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

    public ItemStack getItemStack(){
        return ItemsLoader.getMaurisItem(item, true);
    }

    public static MaurisLoot fromConfigSection(ConfigurationSection section){
        MaurisLoot loot = new MaurisLoot();

        double chance = section.getDouble("chance", 1);

        String itemS = section.getString("item");

        int min = section.getInt("min", 0);
        int max = section.getInt("max", 3);

        loot.setChance(chance);
        loot.setItem(itemS);
        loot.setMinNMaxValue(min,max);

        loot.setName(section.getName());

        return loot;
    }


}
