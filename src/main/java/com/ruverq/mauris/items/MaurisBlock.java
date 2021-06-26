package com.ruverq.mauris.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.blocktypes.MaurisBlockType;
import com.ruverq.mauris.utils.BlockProperty;
import com.ruverq.mauris.utils.BlockStateParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaurisBlock extends MaurisItem {

    public MaurisBlock(MaurisFolder folder, String name, List<String> textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file, MaurisBlockType type, double hardness, HashMap<MaurisItem, Double> hardnessPerTool,  String breakSound,  String placeSound,  String stepSound, MaurisLootTable lootTable) {
        super(folder, name, textures, displayName, lore, material, generateModel, isBlock, maurisBlock, file);
        this.type = type;
        this.hardness = hardness;
        this.hardnessPerTool = hardnessPerTool;
        this.stepSound = stepSound;
        this.breakSound = breakSound;
        this.placeSound = placeSound;
        this.lootTable = lootTable;
    }

    MaurisLootTable lootTable;

    @Getter
    String breakSound;
    @Getter
    String stepSound;
    @Getter
    String placeSound;

    public String getPlaceSoundSafe() {
        if(placeSound == null) return material.createBlockData().getSoundGroup().getPlaceSound().getKey().toString();
        return placeSound;
    }

    public String getBreakSoundSafe() {
        if(breakSound == null) return material.createBlockData().getSoundGroup().getBreakSound().getKey().toString();
        return breakSound;
    }

    public String getStepSoundSafe() {
        if(stepSound == null) return material.createBlockData().getSoundGroup().getStepSound().getKey().toString();
        return stepSound;
    }

    int blockId;

    MaurisBlockType type;

    double hardness;

    HashMap<MaurisItem, Double> hardnessPerTool = new HashMap<>();

    @Override
    public boolean isGenerated(){
        int i = DataHelper.getId(folder, name, "blocks." + type.material().name());
        return i != -1;
    }

    public BlockData getAsBlockData(){
        List<BlockProperty> properties = type.generate(blockId);
        return BlockStateParser.createData(type.material(), properties);
    }

    @Override
    public void generate(){
        MaurisItem item = (MaurisItem) this;
        item.generate("minecraft:block/cube_all");
        System.out.println(id);

        String fff = folder.getName() + ":";

        //First DIR

        JsonObject modelObject = new JsonObject();
        modelObject.addProperty("parent", "minecraft:block/cube_all");

        JsonObject texturesObject = new JsonObject();
        texturesObject.addProperty("all", fff + textures.get(0));
        texturesObject.addProperty("particle", fff + textures.get(0));

        modelObject.add("textures", texturesObject);

        DataHelper.deleteFile("resource_pack/assets/" + folder.getName() +"/models/" + name + ".json");
        DataHelper.createFolder("resource_pack/assets/" + folder.getName() +"/models");
        DataHelper.createFile("resource_pack/assets/" + folder.getName() +"/models/" + name + ".json", modelObject.toString());

        //Second DIR

        JsonObject generalBSObject = new JsonObject();

        JsonArray multipartArray = new JsonArray();
        String blockstatePath = "resource_pack/assets/minecraft/blockstates/" + type.material().name().toLowerCase() + ".json";
        File blockstateFile = DataHelper.getFile(blockstatePath);
        if(blockstateFile != null){
            generalBSObject = DataHelper.FileToJson(blockstateFile);
            multipartArray = generalBSObject.getAsJsonArray("multipart");
        }

        this.blockId = DataHelper.addId(folder, name, "blocks." + type.material().name());

        List<BlockProperty> properties = type.generate(blockId);
        JsonObject mPObject = new JsonObject();
        JsonObject whenObject = new JsonObject();
        JsonObject applyObject = new JsonObject();
        for(BlockProperty property : properties){
            whenObject = property.smartAdd(whenObject);
        }

        mPObject.add("when", whenObject);

        applyObject.addProperty("model", fff + name);
        mPObject.add("apply", applyObject);

        multipartArray.add(mPObject);

        generalBSObject.add("multipart", multipartArray);

        DataHelper.deleteFile(blockstatePath);
        DataHelper.createFolder("resource_pack/assets/minecraft/blockstates/");
        DataHelper.createFile(blockstatePath, generalBSObject.toString());
    }
}
