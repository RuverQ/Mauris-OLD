package com.ruverq.mauris.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.blocktypes.MaurisBlockType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MaurisBlock extends MaurisItem {

    public MaurisBlock(MaurisFolder folder, String name, List<String> textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file) {
        super(folder, name, textures, displayName, lore, material, generateModel, isBlock, maurisBlock, file);
    }

    MaurisBlockType type;

    double hardness;

    HashMap<MaurisItem, Double> hardnessPerTool = new HashMap<>();

    @Override
    public boolean isGenerated(){
        int i = DataHelper.getId(folder, name, "blocks." + type.material().name());
        return i != -1;
    }
}
