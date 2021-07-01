package com.ruverq.mauris.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.utils.unicode.UnicodeUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class MaurisIcon extends MaurisItem {

    public MaurisIcon(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, boolean isBlock, MaurisBlock maurisBlock, File file, int height, int ascent) {
        super(folder, name, textures, displayName, lore, material, generateModel, isBlock, maurisBlock, file);
    }

    @Getter
    Character symbol;

    int symbolId;

    @SneakyThrows
    @Override
    public void generate(){
        System.out.println("a");
        if(textures == null || textures.getTextures().isEmpty()) return;
        System.out.println("b");
        File file = DataHelper.getFile("resource_pack/assets/minecraft/font/default.json");
        JsonObject main = new JsonObject();

        JsonArray providers = new JsonArray();
        if(file != null){
            main = DataHelper.FileToJson(file);
            if(main == null) main = new JsonObject();
            providers = getOrEmptyArray(main, "providers");
        }

        main.remove("providers");

        JsonObject provider = new JsonObject();
        String texturePath = folder.getName() + ":" + textures.getTextures().get(0);
        provider.addProperty("file", texturePath);

        this.symbolId = providers.size();

        JsonArray chars = new JsonArray();

        symbol = UnicodeUtils.getPossibleSymbols().get(symbolId);
        //String hexSymbol = String.format("%04x", (int) symbol);

        int i = 0;
        for(JsonElement providerObject : providers){
            String a = providerObject.getAsJsonObject().get("file").getAsString();
            if(a.equals(texturePath)){
                this.symbolId = i;
                symbol = UnicodeUtils.getPossibleSymbols().get(symbolId);
                return;
            }
            i++;
        }

        chars.add(symbol);
        provider.add("chars", chars);

        File textureFile = DataHelper.getFile("resource_pack/assets/" + folder.getName() + "/textures/" + textures.getTextures().get(0));
        if(textureFile == null) return;


        BufferedImage bimage = ImageIO.read(textureFile);
        int height = bimage.getHeight();
        int ascent = bimage.getWidth();

        provider.addProperty("height", height);
        provider.addProperty("ascent", ascent);
        provider.addProperty("type", "bitmap");

        providers.add(provider);

        main.add("providers", providers);

        DataHelper.deleteFile("resource_pack/assets/minecraft/font/default.json");
        DataHelper.createFolder("resource_pack/assets/minecraft/font");
        DataHelper.createFile("resource_pack/assets/minecraft/font/default.json", main.toString());
    }

    private JsonArray getOrEmptyObject(JsonObject jsonObject, String string){
        if(jsonObject == null) return new JsonArray();

        JsonArray ja = jsonObject.getAsJsonArray(string);
        if(ja == null) return new JsonArray();
        return ja;
    }

    private JsonArray getOrEmptyArray(JsonObject jsonObject, String string){
        if(jsonObject == null) return new JsonArray();

        JsonArray ja = jsonObject.getAsJsonArray(string);
        if(ja == null) return new JsonArray();
        return ja;
    }

    protected static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){
        boolean enabled = cs.getBoolean("icon.enabled");
        mb.setIcon(enabled);

        int height = cs.getInt("icon.height");
        int ascent = cs.getInt("icon.ascent");

        mb.setIconHeight(height);
        mb.setIconAscent(ascent);
    }

}
