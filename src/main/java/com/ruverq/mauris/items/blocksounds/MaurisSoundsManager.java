package com.ruverq.mauris.items.blocksounds;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.blocktypes.MaurisBlockTypeManager;
import com.ruverq.mauris.items.blocktypes.MaurisNoteBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.xml.crypto.Data;
import java.io.File;

public class MaurisSoundsManager {

    static boolean stepSoundEnabled;
    static boolean fallSoundEnabled;
    static boolean hitSoundEnabled;

    public static void setUp(){

        ConfigurationSection config = Mauris.getInstance().getConfig();

        stepSoundEnabled = config.getBoolean("block.sounds.stepSound.enabled");
        fallSoundEnabled = config.getBoolean("block.sounds.fallSound.enabled");
        hitSoundEnabled = config.getBoolean("block.sounds.hitSound.enabled");

        setUpSoundsJson();

        Bukkit.getPluginManager().registerEvents(new StepListener(), Mauris.getInstance());

    }



    private static void setUpSoundsJson(){
        JsonObject soundsJson = new JsonObject();

        String soundJsonPath = "resource_pack/assets/minecraft/sounds.json";
        File file = DataHelper.getFile(soundJsonPath);
        if(file != null){
            soundsJson = DataHelper.FileToJson(file);
            if(soundsJson == null) soundsJson = new JsonObject();
        }

        if(MaurisBlockTypeManager.isEnabled("NOTE_BLOCK")){
            if(stepSoundEnabled){
                soundsJson.add("block.wood.step", getReplaceObject());
                soundsJson.add("replace.block.wood.step", getDefaultSounds("step/wood", 6));
            }

            if(fallSoundEnabled){
                soundsJson.add("block.wood.fall", getReplaceObject());
                soundsJson.add("replace.block.wood.fall", getDefaultSounds("step/wood", 6));
            }

            if(hitSoundEnabled){
                soundsJson.add("block.wood.hit", getReplaceObject());
                soundsJson.add("replace.block.wood.hit", getDefaultSounds("step/wood", 6));
            }
        }

        DataHelper.deleteFile(soundJsonPath);
        DataHelper.createFile(soundJsonPath, soundsJson.toString());
    }

    private static JsonObject getDefaultSounds(String start, int max){
        JsonArray defaultSounds = new JsonArray();
        for(int i = 1 ; i < max + 1; i++){
            defaultSounds.add(start + i);
        }

        JsonObject soundsObject = new JsonObject();
        soundsObject.add("sounds", defaultSounds);

        return soundsObject;
    }

    private static JsonObject getReplaceObject(){
        JsonObject object = new JsonObject();

        object.addProperty("replace", true);
        object.add("sounds", new JsonArray());

        return object;
    }

}
