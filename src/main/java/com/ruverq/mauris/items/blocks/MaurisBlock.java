package com.ruverq.mauris.items.blocks;

import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.*;
import com.ruverq.mauris.items.blocks.blocksounds.BlockSounds;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockType;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import com.ruverq.mauris.utils.BlockProperty;
import com.ruverq.mauris.utils.BlockStateParser;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MaurisBlock extends MaurisItem {

    public MaurisBlock(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, String model, boolean isBlock, MaurisBlock maurisBlock, File file, MaurisBlockType type, int hardness, HashMap<ItemStack, Integer> hardnessPerTool, BlockSounds sounds, MaurisLootTable lootTable, boolean selfDrop, double chanceToBeBlownUp) {
        super(folder, name, textures, displayName, lore, material, generateModel, model, isBlock, maurisBlock, file);
        this.type = type;
        this.hardness = hardness;
        this.hardnessPerTool = hardnessPerTool;
        this.sounds = sounds;
        this.lootTable = lootTable;
        this.selfDrop = selfDrop;
        this.chanceToBeBlownUp = chanceToBeBlownUp;
    }

    @Getter
    boolean selfDrop;

    @Getter
    MaurisLootTable lootTable;

    @Getter
    BlockSounds sounds;

    @Getter
    double chanceToBeBlownUp;

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

    public BlockData getAsBlockData(){
        List<BlockProperty> properties = type.generate(blockId);
        return BlockStateParser.createData(type.material(), properties);
    }

    @Override
    public void generateId(){
        id = DataHelper.addId(getFolder(), getName(), "items." + getMaterial().name());
        blockId = DataHelper.addId(getFolder(), getName(), "blocks." + type.material().name());
    }

    public static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){

        ConfigurationSection blockcs = cs.getConfigurationSection("block");
        if(blockcs == null) return;

        int hardness = blockcs.getInt("hardness.default");
        if(hardness <= 0){
            hardness = blockcs.getInt("hardness", 20);
        }

        String placeSound = blockcs.getString("sounds.place");
        String stepSound = blockcs.getString("sounds.step");
        String breakSound = blockcs.getString("sounds.break");
        String hitSound = blockcs.getString("sounds.hit");
        String fallSound = blockcs.getString("sounds.fall");

        String typeS = blockcs.getString("type");
        boolean selfDrop = blockcs.getBoolean("selfDrop");

        MaurisBlockType type = MaurisBlockTypeManager.getType(typeS);

        BlockSounds sounds = new BlockSounds();
        sounds.setPlaceSound(placeSound);
        sounds.setStepSound(stepSound);
        sounds.setBreakSound(breakSound);
        sounds.setHitSound(hitSound);
        sounds.setFallSound(fallSound);
        sounds.setDefaultSounds(type.material().createBlockData().getSoundGroup());

        double chanceToBeBlownUp = blockcs.getDouble("chanceToBeBlownUp", 1.0);

        mb.setHardness(hardness)
                .setType(type)
                .setSelfDrop(selfDrop)
                .isBlock(true)
                .setSounds(sounds)
                .setChanceToBeBlownUp(chanceToBeBlownUp);

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

        // ah.. this does not work... okay..
        MaurisItem item = this;
        // idk how to reformat this
        item.generate("minecraft:block/cube_all");

        MaurisTextures textures = getTextures();
        MaurisFolder folder = getFolder();


        String modelPath = folder.getName() + "/generated/" + getName();
        //First DIR
        if(isGenerateModel()){

            JsonObject modelObject = new JsonObject();
            modelObject.addProperty("parent", "block/cube_all");

            JsonObject texturesObject = textures.getAsBlockJsonObject(folder.getName());

            modelObject.add("textures", texturesObject);

            DataHelper.deleteFile("resource_pack/assets/minecraft/models/" + folder.getName() + "/generated/" + getName() + ".json");
            DataHelper.createFolder("resource_pack/assets/minecraft/models/" + folder.getName() +"/generated");
            DataHelper.createFile("resource_pack/assets/minecraft/models/" + modelPath + ".json", modelObject.toString());
        }else{
            modelPath = folder.getName() + "/" + getModel();
        }

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
        propertiesObject.addProperty("model", modelPath);

        variantsObject.add(BlockStateParser.createFormattedData(properties), propertiesObject);

        generalBSObject.add("variants", variantsObject);

        DataHelper.deleteFile(blockstatePath);
        DataHelper.createFolder("resource_pack/assets/minecraft/blockstates/");
        DataHelper.createFile(blockstatePath, generalBSObject.toString());
    }
}
