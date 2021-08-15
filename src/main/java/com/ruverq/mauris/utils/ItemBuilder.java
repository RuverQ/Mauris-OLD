package com.ruverq.mauris.utils;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemBuilder {

    String displayName = "ERROR";
    List<String> lore = new ArrayList<>();
    Material material = Material.SADDLE;
    int customModelData = -1;

    HashMap<String, String> nbtStringKeys = new HashMap<>();
    HashMap<String, Integer> nbtIntegerKeys = new HashMap<>();

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = format(displayName);
        return this;
    }

    public ItemBuilder addLore(String line) {
        lore.add(format(line));
        return this;
    }

    public ItemBuilder addNBTTag(String key, String value){
        nbtStringKeys.put(key, value);
        return this;
    }

    public ItemBuilder addNBTTag(String key, int value){
        nbtIntegerKeys.put(key, value);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        List<String> loreColored = new ArrayList<>();
        for(String line : lore){
            loreColored.add(format(line));
        }

        this.lore = loreColored;
        return this;
    }

    public ItemBuilder setMaterial(String material) {
        this.material = Material.matchMaterial(material);
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public static ItemStack fastItemStatic(String displayName, Material material, String... lore){
        ItemStack itemStack = new ItemStack(material);
        return ItemBuilder.fastItemStatic(displayName, itemStack, lore);
    }

    public static ItemStack fastItemStatic(String displayName, ItemStack from, String... lore){
        ItemStack item = from.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(format(displayName));

        List<String> loreList = new ArrayList<>();
        for(String lorinka : lore){
            loreList.add(format(lorinka));
        }
        itemMeta.setLore(loreList);

        item.setItemMeta(itemMeta);

        return item;
    }

    public ItemBuilder fastItem(String displayName, Material material, String... lore){
        setDisplayName(displayName);
        setMaterial(material);

        for(String line : lore){
            addLore(line);
        }

        return this;
    }

    public static String getNBTTag(ItemStack item, String key){
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if(!nmsItem.hasTag()) return null;
        NBTTagCompound tag = nmsItem.getTag().getCompound("mauris");
        if(tag == null) return null;
        return tag.getString(key);
    }

    public ItemStack build(){
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        if(customModelData > 0){
            meta.setCustomModelData(customModelData);
        }

        itemStack.setItemMeta(meta);

        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound maurisCompound = new NBTTagCompound();

        nbtStringKeys.forEach(maurisCompound::setString);
        nbtIntegerKeys.forEach(maurisCompound::setInt);

        tag.set("mauris", maurisCompound);

        itemStack = CraftItemStack.asBukkitCopy(nmsItem);

        return itemStack;
    }

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static String format(String msg){
        Matcher matcher = pattern.matcher(msg);
        while(matcher.find()){
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, String.valueOf(ChatColor.of(color)));
            matcher = pattern.matcher(msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
