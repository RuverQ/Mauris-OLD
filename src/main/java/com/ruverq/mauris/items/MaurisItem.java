package com.ruverq.mauris.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    List<String> textures;
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
    @Getter
    MaurisBlock maurisBlock;

    @Getter
    File file;

    int id;

    public MaurisItem(MaurisFolder folder, String name, List<String> textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file) {
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
        System.out.println(name + " " + id);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
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
                mb.isBlock(true);
            }

            mb
                    .setDisplayName(displayname)
                    .setLore(lore)
                    .setName(name)
                    .setFolder(folder)
                    .setMaterial(material)
                    .setGenerateModel(generateModel)
                    .setTextures(textures);


            list.add(mb.build());
        }

        return list;

    }

    public boolean isGenerated(){
        int i = DataHelper.getId(folder, name, "items." + material.name());
        return i != -1;
    }

    public void generate(){

        String folderName = getFolder().getName();

        if(isGenerated()){
            DataHelper.deleteFile("resource_pack/assets/" + folderName + "/models/" + name + ".json");
        }

        if(generateModel){

            //First DIR
            JsonObject customItemJson = new JsonObject();
            customItemJson.addProperty("parent", "minecraft:item/generated");

            JsonObject jsonTextures = new JsonObject();
            int layeri = 0;
            String fff = folderName + ":";

            for(String tex : textures){

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

            // TODO 2 предмета не работает

            JsonArray overrides = new JsonArray();

            this.id = DataHelper.addId(getFolder(), name, "items." + material.name());

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

            System.out.println("newid " + id);
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

}
