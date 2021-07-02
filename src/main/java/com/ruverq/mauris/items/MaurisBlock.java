package com.ruverq.mauris.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.blocktypes.MaurisBlockType;
import com.ruverq.mauris.items.blocktypes.MaurisBlockTypeManager;
import com.ruverq.mauris.utils.BlockProperty;
import com.ruverq.mauris.utils.BlockStateParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaurisBlock extends MaurisItem {

    public MaurisBlock(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file, MaurisBlockType type, int hardness, HashMap<ItemStack, Integer> hardnessPerTool,  String breakSound,  String placeSound,  String stepSound, MaurisLootTable lootTable, boolean selfDrop) {
        super(folder, name, textures, displayName, lore, material, generateModel, isBlock, maurisBlock, file);
        this.type = type;
        this.hardness = hardness;
        this.hardnessPerTool = hardnessPerTool;
        this.stepSound = stepSound;
        this.breakSound = breakSound;
        this.placeSound = placeSound;
        this.lootTable = lootTable;
        this.selfDrop = selfDrop;
    }

    @Getter
    boolean selfDrop;

    @Getter
    MaurisLootTable lootTable;

    @Getter
    String breakSound;
    @Getter
    String stepSound;
    @Getter
    String placeSound;

    public String getPlaceSoundSafe() {
        if(placeSound == null) return type.material().createBlockData().getSoundGroup().getPlaceSound().getKey().toString();
        return placeSound;
    }

    public String getBreakSoundSafe() {
        if(breakSound == null) return type.material().createBlockData().getSoundGroup().getBreakSound().getKey().toString();
        return breakSound;
    }

    public String getStepSoundSafe() {
        if(stepSound == null) return type.material().createBlockData().getSoundGroup().getStepSound().getKey().toString();
        return stepSound;
    }

    @Getter
    int blockId;

    @Getter
    MaurisBlockType type;

    @Getter
    int hardness;

    HashMap<ItemStack, Integer> hardnessPerTool;

    public int getHardnessFromTool(ItemStack itemStack){
        Object hardnessTool = hardnessPerTool.get(itemStack);
        if(hardnessTool == null) return hardness;

        return (int) hardnessTool;
    }

    @Override
    public boolean isGenerated(){
        int i = DataHelper.getId(folder, name, "blocks." + type.material().name());
        return i != -1;
    }

    public BlockData getAsBlockData(){
        List<BlockProperty> properties = type.generate(blockId);
        return BlockStateParser.createData(type.material(), properties);
    }

    protected static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){

        ConfigurationSection blockcs = cs.getConfigurationSection("block");
        if(blockcs == null) return;

        int hardness = blockcs.getInt("hardness.default");
        if(hardness <= 0){
            hardness = blockcs.getInt("hardness", 20);
        }

        String placeSound = blockcs.getString("sounds.place");
        String stepSound = blockcs.getString("sounds.step");
        String breakSound = blockcs.getString("sounds.break");

        String typeS = blockcs.getString("type");
        boolean selfDrop = blockcs.getBoolean("selfDrop");

        MaurisBlockType type = MaurisBlockTypeManager.getType(typeS);

        mb.setHardness(hardness)
                .setType(type)
                .setSelfDrop(selfDrop)
                .isBlock(true)
                .setSounds(breakSound, placeSound, stepSound);

        ConfigurationSection blTexCs = blockcs.getConfigurationSection("textures");
        if(blTexCs != null && blTexCs.getKeys(false).size() > 0){
            MaurisTextures mTextures = new MaurisTextures().loadFromConfigSection(blTexCs);
            mb.setTextures(mTextures);
        }else{
            MaurisTextures mTextures = new MaurisTextures();
            mTextures.setTextures(cs.getStringList("textures"));
            mb.setTextures(mTextures);
        }

        ConfigurationSection hardnessCS = blockcs.getConfigurationSection("hardness");
        if(hardnessCS != null){
            for(String hardnessToolS : hardnessCS.getKeys(false)){
                if(hardnessToolS.equalsIgnoreCase("default")) continue;

                ItemStack itemStack = ItemsLoader.getMaurisItem(hardnessToolS, true);
                if(itemStack == null) continue;
                int tempHardness = blockcs.getInt("hardness." + hardnessToolS);

                mb.addHardnessPerTool(itemStack, tempHardness);
            }
        }

        MaurisLootTable lootTable = MaurisLootTable.fromConfigSection(blockcs.getConfigurationSection("lootTable"));
        mb.setLootTable(lootTable);
    }


    @Override
    public void generate(){
        MaurisItem item = this;
        item.generate("minecraft:block/cube_all");

        this.blockId = DataHelper.addId(folder, name, "blocks." + type.material().name());

        String fff = folder.getName() + ":";

        //First DIR

        JsonObject modelObject = new JsonObject();
        modelObject.addProperty("parent", "minecraft:block/cube_all");

        JsonObject texturesObject = textures.getAsBlockJsonObject(folder.getName());

        modelObject.add("textures", texturesObject);

        DataHelper.deleteFile("resource_pack/assets/" + folder.getName() +"/models/generated/" + name + ".json");
        DataHelper.createFolder("resource_pack/assets/" + folder.getName() +"/models/generated");
        DataHelper.createFile("resource_pack/assets/" + folder.getName() +"/models/generated/" + name + ".json", modelObject.toString());

        //Second DIR
        JsonObject generalBSObject = new JsonObject();

        String blockstatePath = "resource_pack/assets/minecraft/blockstates/" + type.material().name().toLowerCase() + ".json";

        JsonObject variantsObject = new JsonObject();
        File blockstateFile = DataHelper.getFile(blockstatePath);
        if(blockstateFile != null){
            generalBSObject = DataHelper.FileToJson(blockstateFile);
            variantsObject = generalBSObject.getAsJsonObject("variants");
        }

        List<BlockProperty> properties = type.generate(blockId);

        JsonObject propertiesObject = new JsonObject();
        propertiesObject.addProperty("model", fff + "generated/" + name);

        variantsObject.add(BlockStateParser.createFormattedData(properties), propertiesObject);

        generalBSObject.add("variants", variantsObject);

        DataHelper.deleteFile(blockstatePath);
        DataHelper.createFolder("resource_pack/assets/minecraft/blockstates/");
        DataHelper.createFile(blockstatePath, generalBSObject.toString());
    }
}
