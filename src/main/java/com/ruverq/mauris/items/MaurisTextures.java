package com.ruverq.mauris.items;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class MaurisTextures {

    @Getter
    @Setter
    List<String> textures = new ArrayList<>();

    @Getter
    @Setter
    String topTexture;

    @Getter
    @Setter
    String bottomTexture;

    @Getter
    @Setter
    String southTexture;

    @Getter
    @Setter
    String northTexture;

    @Getter
    @Setter
    String westTexture;

    @Getter
    @Setter
    String eastTexture;

    public void addTexture(String texture){
        textures.add(texture);
    }

    public void addTexture(String side, String texture){
        textures.add(texture);

        if(side.equalsIgnoreCase("top")) setTopTexture(texture);
        if(side.equalsIgnoreCase("bottom")) setBottomTexture(texture);
        if(side.equalsIgnoreCase("west")) setWestTexture(texture);
        if(side.equalsIgnoreCase("east")) setEastTexture(texture);
        if(side.equalsIgnoreCase("south")) setSouthTexture(texture);
        if(side.equalsIgnoreCase("north")) setNorthTexture(texture);
    }

    public JsonObject getAsBlockJsonObject(String namespace){

        String add = namespace + ":";

        JsonObject jsonObject = new JsonObject();

        if(textures.isEmpty()){
            return jsonObject;
        }

        if(textures.size() == 1 || ifEverySpecificTextureIsNull()){
            jsonObject.addProperty("all", add + textures.get(0));
            jsonObject.addProperty("particle", add + textures.get(0));
            return jsonObject;
        }

        jsonObject.addProperty("particle", add + textures.get(0));

        if(topTexture != null) jsonObject.addProperty("up", add + topTexture);
        if(bottomTexture != null) jsonObject.addProperty("down", add + bottomTexture);
        if(southTexture != null) jsonObject.addProperty("south", add + southTexture);
        if(westTexture != null) jsonObject.addProperty("west", add + westTexture);
        if(eastTexture != null) jsonObject.addProperty("east", add + eastTexture);
        if(northTexture != null ) jsonObject.addProperty("north", add + northTexture);

        return jsonObject;
    }

    private boolean ifEverySpecificTextureIsNull(){
        return topTexture == null && bottomTexture == null && southTexture == null && westTexture == null && eastTexture == null && northTexture == null;
    }

    public MaurisTextures loadFromConfigSection(ConfigurationSection cs){
        if(cs.isList("")){
            for(String texture : cs.getStringList("")){
                addTexture(texture);
            }
            return this;
        }

        String topTexture = cs.getString("top");
        String bottomTexture = cs.getString("bottom");
        String southTexture = cs.getString("south");
        String westTexture = cs.getString("west");
        String eastTexture = cs.getString("east");
        String northTexture = cs.getString("north");
        if(topTexture != null && !topTexture.isEmpty()) addTexture("top", topTexture);
        if(bottomTexture != null && !bottomTexture.isEmpty()) addTexture("bottom", bottomTexture);
        if(southTexture != null && !southTexture.isEmpty()) addTexture("south", southTexture);
        if(westTexture != null && !westTexture.isEmpty()) addTexture("west", westTexture);
        if(eastTexture != null && !eastTexture.isEmpty()) addTexture("east", eastTexture);
        if(northTexture != null && !northTexture.isEmpty()) addTexture("north", northTexture);

        return this;
    }

}
