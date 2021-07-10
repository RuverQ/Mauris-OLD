package com.ruverq.mauris.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.icons.MaurisIcon;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    MaurisBlock maurisBlock;

    @Getter
    File file;

    @Getter
    boolean isBlock;

    @Getter
    protected int id;

    public MaurisItem(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file) {
        this.folder = folder;
        this.name = name;
        this.textures = textures;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.generateModel = generateModel;
        this.maurisBlock = maurisBlock;
        this.isBlock = isBlock;
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

    public static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){

        String displayname = cs.getString("displayname");
        String material = cs.getString("material");
        boolean generateModel = cs.getBoolean("generateModel");
        List<String> lore = cs.getStringList("lore");
        List<String> textures = cs.getStringList("textures");

        mb.setTextures(textures);

        mb
                .setDisplayName(displayname)
                .setLore(lore)
                .setName(cs.getName())
                .setMaterial(material)
                .setGenerateModel(generateModel);

        return;
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
            mb.setName(name);
            mb.setFolder(folder);

            ConfigurationSection itemSection = cs.getConfigurationSection(name);

            MaurisIcon.loadFromConfigurationSection(mb, itemSection);
            MaurisItem.loadFromConfigurationSection(mb, itemSection);
            MaurisBlock.loadFromConfigurationSection(mb, itemSection);

            list.add(mb.build());
        }

        return list;
    }

    public void generate(){
        generate("minecraft:item/generated");
    }

    public void generateId(){
        id = DataHelper.addId(getFolder(), getName(), "items." + material.name());
    }

    public static void generate(List<MaurisItem> maurisItems){

        HashMap<String, MaurisItem> items = new HashMap<>();
        for(MaurisItem item : maurisItems){
            item.generateId();
            System.out.println("Added: " + item.getFolder().getName() + ":" + item.getName());
            items.put(item.getFolder().getName() + ":" + item.getName(), item);
        }

        ConfigurationSection ids = DataHelper.getFileAsYAML("ids.yml");
        for(String section : ids.getConfigurationSection("items").getKeys(false)){
            Material material = Material.matchMaterial(section);
            DataHelper.deleteFile("resource_pack/assets/minecraft/models/item/" + material.name().toLowerCase() + ".json");

            for(String item : ids.getConfigurationSection("items." + section).getKeys(false)){
                MaurisItem mitem = items.get(item);
                if(mitem != null) System.out.println("Loaded: " + mitem.getFolder().getName() + ":" + mitem.getName());
                mitem.generate();
            }
        }

        for(String otherSection : ids.getConfigurationSection("").getKeys(false)){
            if(otherSection.equalsIgnoreCase("items")) continue;
            for(String section : ids.getConfigurationSection(otherSection).getKeys(false)){
                for(String otherItem : ids.getConfigurationSection(otherSection + "." + section).getKeys(false)){
                    items.get(otherItem).generate();
                }
            }
        }
    }

    public void generate(String parent){

        String folderName = getFolder().getName();

        if(!generateModel) return;

        //First DIR
        JsonObject customItemJson = new JsonObject();
        customItemJson.addProperty("parent", parent);

        JsonObject jsonTextures = new JsonObject();
        int layeri = 0;

        for(String tex : textures.getTextures()){

            jsonTextures.addProperty("layer" + layeri, folderName + "/" + tex);
            layeri++;
        }

        customItemJson.add("textures", jsonTextures);

        DataHelper.createFolder("resource_pack/assets/minecraft/models/" + folderName + "/generated");
        DataHelper.createFolder("resource_pack/assets/minecraft/textures/" + folderName);
        DataHelper.createFile("resource_pack/assets/minecraft/models/" + folderName + "/generated/" + name + ".json", customItemJson.toString());

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
                if(model.equalsIgnoreCase(name)) {
                    return;
                }
            }
        }else{
            overrides.add(getDefaultPaperObject());
        }

        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();

        predicate.addProperty("custom_model_data", id);
        override.add("predicate", predicate);
        override.addProperty("model", folderName + "/generated/" + name);

        overrides.add(override);

        itemJson.remove("overrides");
        itemJson.add("overrides", overrides);

        DataHelper.createFolder("resource_pack/assets/minecraft/models/item");
        DataHelper.deleteFile("resource_pack/assets/minecraft/models/item/" + material.name().toLowerCase() + ".json");
        DataHelper.createFile("resource_pack/assets/minecraft/models/item/" + material.name().toLowerCase() + ".json", itemJson.toString());
    }

    private JsonObject getDefaultPaperObject(){
        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();

        predicate.addProperty("custom_model_data", 0);

        override.addProperty("model", "minecraft:item/" + material.name().toLowerCase());

        override.add("predicate", predicate);

        return override;
    }

}
