package com.ruverq.mauris.items;

import com.ruverq.mauris.items.blocktypes.MaurisBlockType;
import org.bukkit.Material;

import java.io.File;
import java.util.List;

public class MaurisBuilder {

    MaurisFolder folder;

    String name;

    List<String> textures;
    String displayName;
    List<String> lore;
    Material material;
    boolean generateModel;

    boolean isBlock;
    MaurisBlock maurisBlock;

    File file;

    double hardness;

    String stepSound;
    String placeSound;
    String breakSound;

    MaurisBlockType type;

    MaurisLootTable lootTable;

    public MaurisItem build(){
        if(isBlock) return new MaurisBlock(folder,name,textures,displayName,lore,material,generateModel, true,maurisBlock,file, type, hardness, null, breakSound, placeSound, stepSound, lootTable);
        return new MaurisItem(folder,name,textures,displayName,lore,material,generateModel, false,maurisBlock, file);
    }

    public MaurisBuilder setBlock(MaurisBlock block){
        this.maurisBlock = block;
        return this;
    }

    public MaurisBuilder setLootTable(MaurisLootTable table){
        this.lootTable = table;
        return this;
    }

    public MaurisBuilder addTexture(String path){
        textures.add(path);
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

    public MaurisBuilder setTextures(List<String> textures){
        this.textures = textures;
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

    public MaurisBuilder setHardness(double hardness){
        this.hardness = hardness;
        return this;
    }

    public MaurisBuilder setType(MaurisBlockType type){
        this.type = type;
        return this;
    }

}
