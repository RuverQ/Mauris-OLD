package com.ruverq.mauris.items;

import com.ruverq.mauris.items.blocktypes.MaurisBlockType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MaurisBuilder {

    MaurisFolder folder;

    String name;

    MaurisTextures textures;
    String displayName;
    List<String> lore;
    Material material;
    boolean generateModel;

    boolean isBlock;
    MaurisBlock maurisBlock;

    File file;

    int hardness;

    String stepSound;
    String placeSound;
    String breakSound;

    boolean selfDrop;

    MaurisBlockType type;

    MaurisLootTable lootTable;

    HashMap<ItemStack, Integer> hardnessPerTool = new HashMap<>();

    int iconHeight;
    int iconAscent;
    boolean isIcon;

    public MaurisItem build(){
        if(isIcon) return new MaurisIcon(folder, name, textures, displayName, lore, material, generateModel, isBlock, maurisBlock, file, iconHeight, iconAscent);
        if(isBlock) return new MaurisBlock(folder,name,textures,displayName,lore,material,generateModel, true,maurisBlock,file, type, hardness, hardnessPerTool, breakSound, placeSound, stepSound, lootTable, selfDrop);
        return new MaurisItem(folder,name,textures,displayName,lore,material,generateModel, false,maurisBlock, file);
    }

    public void addHardnessPerTool(ItemStack itemStack, int hardness){
        hardnessPerTool.put(itemStack, hardness);
    }

    public void addHardnessPerTool(MaurisItem item, int hardness){
        hardnessPerTool.put(item.getAsItemStack(), hardness);
    }

    public MaurisBuilder setSelfDrop(boolean selfDrop){
        this.selfDrop = selfDrop;
        return this;
    }

    public MaurisBuilder enableSelfDrop(){
        selfDrop = true;
        return this;
    }

    public MaurisBuilder setBlock(MaurisBlock block){
        this.maurisBlock = block;
        return this;
    }

    public MaurisBuilder setLootTable(MaurisLootTable table){
        this.lootTable = table;
        return this;
    }

    public MaurisBuilder addTexture(String side, String path){

        if(textures == null){
            MaurisTextures mTextures = new MaurisTextures();
            mTextures.addTexture(side, path);

            this.textures = mTextures;
        }else{
            textures.addTexture(side, path);
        }

        return this;
    }

    public MaurisBuilder addTexture(String path){
        addTexture("asdijha0oisdhoasjdopahsd", path);
        return this;
    }

    public MaurisBuilder setIcon(boolean isIcon) {
        this.isIcon = isIcon;
        return this;
    }

    public MaurisBuilder setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
        return this;
    }

    public MaurisBuilder setIconAscent(int iconAscent) {
        this.iconAscent = iconAscent;
        return this;
    }

    public MaurisBuilder setDisplayName(String displayName){
        this.displayName = displayName;
        return this;
    }

    public MaurisBuilder addLore(String line){
        lore.add(line);
        return this;
    }

    public MaurisBuilder setTextures(MaurisTextures textures){
        this.textures = textures;
        return this;
    }

    public MaurisBuilder setTextures(List<String> textures){
        MaurisTextures mTextures = new MaurisTextures();
        mTextures.setTextures(textures);
        this.textures = mTextures;
        return this;
    }

    public MaurisBuilder setLore(List<String> lines){
        this.lore = lines;
        return this;
    }

    public MaurisBuilder setMaterial(Material material){
        this.material = material;
        return this;
    }

    public MaurisBuilder setMaterial(String material){
        if(material == null) return this;
        this.material = Material.matchMaterial(material);
        return this;
    }

    public MaurisBuilder setFile(File file){
        this.file = file;
        return this;
    }

    public MaurisBuilder setSounds(String breakSound, String placeSound, String stepSound){
        this.breakSound = breakSound;
        this.placeSound = placeSound;
        this.stepSound = stepSound;
        return this;
    }

    public MaurisBuilder setBreakSound(String breakSound){
        this.breakSound = breakSound;
        return this;
    }

    public MaurisBuilder setPlaceSound(String placeSound){
        this.placeSound = placeSound;
        return this;
    }

    public MaurisBuilder setStepSound(String stepSound){
        this.stepSound = stepSound;
        return this;
    }

    public MaurisBuilder setGenerateModel(boolean generateModel){
        this.generateModel = generateModel;
        return this;
    }

    public MaurisBuilder isBlock(boolean isBlock){
        this.isBlock = isBlock;
        return this;
    }

    public MaurisBuilder setName(String name){
        this.name = name;
        return this;
    }

    public MaurisBuilder setFolder(MaurisFolder folder){
        this.folder = folder;
        return this;
    }

    public MaurisBuilder setFolder(String folder){
        this.folder = new MaurisFolder(folder);
        return this;
    }

    public MaurisBuilder setHardness(int hardness){
        this.hardness = hardness;
        return this;
    }

    public MaurisBuilder setType(MaurisBlockType type){
        this.type = type;
        return this;
    }

}
