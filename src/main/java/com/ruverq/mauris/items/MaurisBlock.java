package com.ruverq.mauris.items;

import org.bukkit.Material;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MaurisBlock extends MaurisItem {

    public MaurisBlock(String folder, String name, List<String> textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file) {
        super(folder, name, textures, displayName, lore, material, generateModel, isBlock, maurisBlock, file);
    }

    enum MaurisBlockType{
        NOTE_BLOCK,
        MUSHROOM,
    }

    MaurisBlockType type;

    double hardness;

    HashMap<MaurisItem, Double> hardnessPerTool = new HashMap<>();

}
