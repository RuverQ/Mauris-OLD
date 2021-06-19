package com.ruverq.mauris.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemBuilder {

    String displayName = "ERROR";
    List<String> lore = new ArrayList<>();
    Material material = Material.SADDLE;

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = format(displayName);
        return this;
    }

    public ItemBuilder addLore(String line) {
        lore.add(format(line));
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

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder fastItem(String displayName, Material material, String... lore){
        setDisplayName(displayName);
        setMaterial(material);

        for(String line : lore){
            addLore(line);
        }

        return this;
    }

    public ItemStack build(){
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);

        itemStack.setItemMeta(meta);

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
