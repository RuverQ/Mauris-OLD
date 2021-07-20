package com.ruverq.mauris.items.icons;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.utils.unicode.UnicodeUtils;
import lombok.Data;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MaurisOffsetIcon {

    public static char offsetForward;
    public static char offsetBackward;


    public static void setUp(){

        String offsetImageName = "space_nosplit";
        createImage(1, 1, offsetImageName);

        String offsetSplitImageName = "space_split";
        createImage(256, 256, offsetSplitImageName);

        String fontDefaultPath = "resource_pack/assets/minecraft/font/default.json";
        File file = DataHelper.getFile(fontDefaultPath);
        JsonObject json = DataHelper.FileToJson(file);

        offsetBackward = createProvider(offsetSplitImageName, json, -3, -32768);
        offsetForward = createProvider(offsetSplitImageName, json, 0, -32768);

        DataHelper.deleteFile(fontDefaultPath);
        DataHelper.createFile(fontDefaultPath, json.toString());
    }

    private static char createProvider(String name, JsonObject json, int height, int ascent){
        JsonArray providers = json.getAsJsonArray("providers");

        JsonObject provider = new JsonObject();
        provider.addProperty("file", ".offset_icons/" + name + ".png");

        int symbolId = providers.size();
        Character symbol = UnicodeUtils.getPossibleSymbols().get(symbolId);

        JsonArray chars = new JsonArray();
        chars.add(symbol);
        provider.add("chars", chars);

        provider.addProperty("height", height);
        provider.addProperty("ascent", ascent);
        provider.addProperty("type", "bitmap");

        providers.add(provider);

        return symbol;
    }



    @SneakyThrows
    private static void createImage(int width, int height, String name){
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graphics = img.createGraphics();

        graphics.setColor( new Color(0, 0, 0, 1) );
        graphics.fillRect ( 0, 0, width, height );

        DataHelper.createFolder("resource_pack/assets/minecraft/textures/.offset_icons");
        File outputfile = DataHelper.getFile("resource_pack/assets/minecraft/textures/.offset_icons/" + name + ".png", true);
        outputfile.createNewFile();

        ImageIO.write(img, "png", outputfile);
    }


}
