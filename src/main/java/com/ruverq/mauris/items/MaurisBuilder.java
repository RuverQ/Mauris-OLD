package com.ruverq.mauris.items;

import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.blocks.MaurisLootTable;
import com.ruverq.mauris.items.blocks.blocksounds.BlockSounds;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockType;
import com.ruverq.mauris.items.hud.GameModeChecker;
import com.ruverq.mauris.items.hud.MaurisHUD;
import com.ruverq.mauris.items.icons.MaurisIcon;
import com.ruverq.mauris.items.musicdisc.MaurisMusicDisc;
import com.ruverq.mauris.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO Change that system
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

    BlockSounds sounds;

    boolean selfDrop;
    double chanceToBeBlownUp;

    MaurisBlockType type;

    MaurisLootTable lootTable;

    HashMap<ItemStack, Integer> hardnessPerTool = new HashMap<>();

    String model;

    // Music Disc

    boolean isMusicDisc;
    String music;
    String displayNameMusic;


    // ICON

    double sizeMultiplier;
    int yOffset;
    MaurisIcon.IconAlign align;
    boolean isIcon;


    //HUD

    int xOffset;
    boolean hudEnabled;
    boolean isHUD;
    List<String> frames;

    boolean vanillaIterator;
    int vanillaIterate;

    List<GameModeChecker> gameModeCheckers;
    boolean underwaterVisibility;

    ItemCharacteristics itemCharacteristics = new ItemCharacteristics();

    public MaurisItem build(){
        if(isHUD) return new MaurisHUD(folder, name, textures,itemCharacteristics, generateModel, model, isBlock, maurisBlock, file, xOffset, hudEnabled, frames, vanillaIterator, vanillaIterate, gameModeCheckers, underwaterVisibility);
        if(isIcon) return new MaurisIcon(folder, name, textures,itemCharacteristics, generateModel, model, isBlock, maurisBlock, file, sizeMultiplier, align, yOffset);
        if(isBlock) return new MaurisBlock(folder,name,textures,itemCharacteristics, generateModel, model, true,maurisBlock,file, type, hardness, hardnessPerTool, sounds, lootTable, selfDrop, chanceToBeBlownUp);
        if(isMusicDisc) return new MaurisMusicDisc(folder,name,textures,itemCharacteristics, generateModel, model, false,maurisBlock, file, music, displayNameMusic);
        return new MaurisItem(folder,name,textures,itemCharacteristics,generateModel, model, false,maurisBlock, file);
    }

    public void addHardnessPerTool(ItemStack itemStack, int hardness){
        hardnessPerTool.put(itemStack, hardness);
    }

    public MaurisBuilder setItemCharacteristics(ItemCharacteristics itemCharacteristics){
        this.itemCharacteristics = itemCharacteristics;
        return this;
    }

    public MaurisBuilder setMusicDisc(boolean enabled){
        this.isMusicDisc = enabled;
        return this;
    }

    public MaurisBuilder setMusicDisplayName(String displayName){
        this.displayNameMusic = displayName;
        return this;
    }

    public MaurisBuilder setMusic(String music){
        this.music = music;
        return this;
    }

    public MaurisBuilder setGameModeCheckers(List<GameModeChecker> gameModeCheckers) {
        this.gameModeCheckers = gameModeCheckers;
        return this;
    }

    public MaurisBuilder setUnderwaterVisibility(boolean underwaterVisibility) {
        this.underwaterVisibility = underwaterVisibility;
        return this;
    }

    public MaurisBuilder setXOffset(int xOffset){
        this.xOffset = xOffset;
        return this;
    }

    public MaurisBuilder setVanillaIterator(boolean vanilaIterator){
        this.vanillaIterator = vanilaIterator;
        return this;
    }

    public MaurisBuilder enableVanillaIterator(){
        vanillaIterator = true;
        return this;
    }

    public MaurisBuilder setVanillaIterate(int count){
        vanillaIterate = count;
        return this;
    }

    public MaurisBuilder setHUD(boolean isHUD){
        this.isHUD = isHUD;
        return this;
    }

    public MaurisBuilder setHudEnabled(boolean enabled){
        this.hudEnabled = enabled;
        return this;
    }

    public MaurisBuilder setFrames(List<String> frames){
        this.frames = frames;
        return this;
    }

    public void addHardnessPerTool(MaurisItem item, int hardness){
        hardnessPerTool.put(item.getAsItemStack(), hardness);
    }

    public MaurisBuilder setChanceToBeBlownUp(double changeToBeBlownUp){
        this.chanceToBeBlownUp = changeToBeBlownUp;
        return this;
    }

    public MaurisBuilder setAlign(MaurisIcon.IconAlign align){
        this.align = align;
        return this;
    }

    public MaurisBuilder setYOffset(int yOffset){
        this.yOffset = yOffset;
        return this;
    }

    public MaurisBuilder setSizeMultiplier(double sizeMultiplier){
        this.sizeMultiplier = sizeMultiplier;
        return this;
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

    public MaurisBuilder setTextures(MaurisTextures textures){
        this.textures = textures;
        return this;
    }

    public MaurisBuilder setModel(String model){
        this.model = model;
        return this;
    }

    public MaurisBuilder setTextures(List<String> textures){
        MaurisTextures mTextures = new MaurisTextures();
        mTextures.setTextures(textures);
        this.textures = mTextures;
        return this;
    }

    public MaurisBuilder setFile(File file){
        this.file = file;
        return this;
    }

    public MaurisBuilder setSounds(BlockSounds sounds){
        this.sounds = sounds;
        return this;
    }

    public MaurisBuilder setSounds(String breakSound, String placeSound, String stepSound, String hitSound){
        BlockSounds bs = new BlockSounds();
        bs.setBreakSound(breakSound);
        bs.setPlaceSound(placeSound);
        bs.setStepSound(stepSound);
        bs.setHitSound(hitSound);
        this.sounds = bs;
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
