package com.ruverq.mauris.items.icons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.*;
import com.ruverq.mauris.items.blocks.MaurisBlock;
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


    public enum IconAlign{
        CENTER,
        BOTTOM,
        TOP;
    }

    public MaurisIcon(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, String model, boolean isBlock, MaurisBlock maurisBlock, File file, double sizeMultiplier, IconAlign align, int yOffset) {
        super(folder, name, textures, displayName, lore, material, generateModel, model, isBlock, maurisBlock, file);
        this.sizeMultiplier = sizeMultiplier;
        this.align = align;
        this.yOffset = yOffset;
    }

    public String getNormalizedSymbol(){
        String hexSymbol = String.format("%04x", (int) this.symbol);
        String symbol = "\\u" + hexSymbol;

        return StringEscapeUtils.unescapeJava(symbol);
    }

    double sizeMultiplier;
    IconAlign align;
    int yOffset;

    @Getter
    Character symbol;

    int symbolId;

    @Override
    public void generateId(){
        id = symbolId;
    }

    @SneakyThrows
    @Override
    public void generate(){
        if(getTextures() == null || getTextures().getTextures().isEmpty()) return;
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
        String texturePath = getFolder().getName() + "/" + getTextures().getTextures().get(0) + ".png";
        provider.addProperty("file", texturePath);

        this.symbolId = providers.size();

        JsonArray chars = new JsonArray();

        this.symbol = UnicodeUtils.getPossibleSymbols().get(symbolId);
        //String hexSymbol = String.format("%04x", (int) symbol);

        int i = 0;
        for(JsonElement providerObject : providers){
            String a = providerObject.getAsJsonObject().get("file").getAsString();
            if(a.equals(texturePath)){
                this.symbolId = i;
                this.symbol = UnicodeUtils.getPossibleSymbols().get(symbolId);
                return;
            }
            i++;
        }

        chars.add(symbol);
        provider.add("chars", chars);

        String texturePathT = "resource_pack/assets/minecraft/textures/" + getFolder().getName() + "/" + getTextures().getTextures().get(0);
        if(!texturePathT.endsWith(".png")){
            texturePathT = texturePathT + ".png";
        }

        File textureFile = DataHelper.getFile(texturePathT);
        if(textureFile == null) return;


        BufferedImage bimage = ImageIO.read(textureFile);
        int height = bimage.getHeight();
        int ascent = 0;

        height = (int) (height * sizeMultiplier);

        if(align == IconAlign.BOTTOM){
            ascent = height;
        }else if(align == IconAlign.CENTER){
            ascent = height / 2;
        }

        ascent += yOffset;

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

    public static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){
        boolean enabled = cs.getBoolean("icon.enabled");
        mb.setIcon(enabled);

        int yOffset = cs.getInt("icon.yOffset");
        double sizeMultiplier = cs.getDouble("icon.sizeMultiplier", 1);
        if(sizeMultiplier < 1){
            sizeMultiplier = 1;
        }

        String alignS = cs.getString("icon.align", "bottom");
        IconAlign align = IconAlign.BOTTOM;
        for(MaurisIcon.IconAlign iconAlign : MaurisIcon.IconAlign.values()){
            if(iconAlign.name().equalsIgnoreCase(alignS)) {
                align = iconAlign;
                break;
            }
        }

        mb.setYOffset(yOffset)
        .setSizeMultiplier(sizeMultiplier)
        .setAlign(align);
    }

}
