package com.ruverq.mauris.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.blocktypes.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ruverq.mauris.utils.FormatUtils.formatColor;
import static com.ruverq.mauris.utils.FormatUtils.formatColorList;

public class MaurisItem {

    @Getter
    MaurisFolder folder;
    @Getter
    String name;

    @Getter
    MaurisTextures textures;
    @Getter
    String displayName;
    @Getter
    List<String> lore;
    @Getter
    Material material;
    @Getter
    boolean generateModel;

    @Getter
    boolean isBlock;
    MaurisBlock maurisBlock;

    @Getter
    File file;

    int id;

    public MaurisItem(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file) {
        this.folder = folder;
        this.name = name;
        this.textures = textures;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.generateModel = generateModel;
        this.isBlock = isBlock;
        this.maurisBlock = maurisBlock;
        this.file = file;
    }

    public ItemStack getAsItemStack(){

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(formatColor(displayName));
        itemMeta.setLore(formatColorList(lore));
        itemMeta.setCustomModelData(id);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public MaurisBlock getAsMaurisBlock(){
        if(this instanceof MaurisBlock) return (MaurisBlock) this;
        return null;
    }

    public static List<MaurisItem> loadFromFile(File file, String folder){
        return loadFromFile(file, new MaurisFolder(folder));
    }

    public static List<MaurisItem> loadFromFile(File file, MaurisFolder folder){
        List<MaurisItem> list = new ArrayList<>();

        ConfigurationSection cs = YamlConfiguration.loadConfiguration(file);

        for(String name : cs.getKeys(false)){

            MaurisBuilder mb = new MaurisBuilder();
            mb.setFile(file);

            String displayname = cs.getString(name + ".displayname");
            String material = cs.getString(name + ".material");
            boolean generateModel = cs.getBoolean(name + ".generateModel");
            List<String> lore = cs.getStringList(name + ".lore");
            List<String> textures = cs.getStringList(name + ".textures");

            if(cs.isConfigurationSection(name + ".block")){
                ConfigurationSection blockcs = cs.getConfigurationSection(name + ".block");

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
                    mTextures.setTextures(textures);
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


            }else{
                mb.setTextures(textures);
            }

            mb
                    .setDisplayName(displayname)
                    .setLore(lore)
                    .setName(name)
                    .setFolder(folder)
                    .setMaterial(material)
                    .setGenerateModel(generateModel);

            list.add(mb.build());
        }

        return list;

    }

    public boolean isGenerated(){
        int i = DataHelper.getId(folder, name, "items." + material.name());
        return i != -1;
    }

    public void generate(){
        generate("minecraft:item/generated");
    }

    public void generate(String parent){

        String folderName = getFolder().getName();

        this.id = DataHelper.addId(getFolder(), name, "items." + material.name());

        if(!generateModel) return;
        if(isGenerated()){
            DataHelper.deleteFile("resource_pack/assets/" + folderName + "/models/" + name + ".json");
        }

        //First DIR
        JsonObject customItemJson = new JsonObject();
        customItemJson.addProperty("parent", parent);

        JsonObject jsonTextures = new JsonObject();
        int layeri = 0;
        String fff = folderName + ":";

        for(String tex : textures.getTextures()){

            jsonTextures.addProperty("layer" + layeri, fff + tex);
            layeri++;
        }

        customItemJson.add("textures", jsonTextures);

        DataHelper.createFolder("resource_pack/assets/" + folderName + "/models");
        DataHelper.createFolder("resource_pack/assets/" + folderName + "/textures");
        DataHelper.createFile("resource_pack/assets/" + folderName + "/models/" + name + ".json", customItemJson.toString());

        //Second DIR
        File itemFileJson = DataHelper.getFile("resource_pack/assets/minecraft/models/item/" + material.name().toLowerCase() + ".json");
        JsonObject itemJson = new JsonObject();
        if(itemFileJson == null){
            itemJson.addProperty("parent", "minecraft:item/generated");
            JsonObject jsonItemTextures = new JsonObject();
            jsonItemTextures.addProperty("layer0", "minecraft:item/" + material.name().toLowerCase());
            itemJson.add("textures", jsonItemTextures);
        }else{
            itemJson = DataHelper.FileToJson(itemFileJson);
        }

        if(itemJson == null){
            Bukkit.getLogger().warning("ERROR");
            return;
        }

        JsonArray overrides = new JsonArray();


        if(itemJson.has("overrides")){
            overrides = itemJson.getAsJsonArray("overrides");
            for (JsonElement element : overrides) {
                JsonObject tempOverride = element.getAsJsonObject();
                if(tempOverride == null) continue;
                JsonElement tempModel = tempOverride.get("model");
                String model = tempModel.getAsString();
                if(model.equalsIgnoreCase(fff + name)) {
                    return;
                }
            }
        }

        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();

        predicate.addProperty("custom_model_data", id);
        override.add("predicate", predicate);
        override.addProperty("model", fff + name);

        overrides.add(override);

        itemJson.remove("overrides");
        itemJson.add("overrides", overrides);

        DataHelper.createFolder("resource_pack/assets/minecraft/models/item");
        DataHelper.deleteFile("resource_pack/assets/minecraft/models/item/" + material.name().toLowerCase() + ".json");
        DataHelper.createFile("resource_pack/assets/minecraft/models/item/" + material.name().toLowerCase() + ".json", itemJson.toString());
    }

}
