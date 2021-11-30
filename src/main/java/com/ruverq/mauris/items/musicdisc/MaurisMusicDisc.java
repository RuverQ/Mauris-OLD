package com.ruverq.mauris.items.musicdisc;

import com.ruverq.mauris.items.*;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;

public class MaurisMusicDisc extends MaurisItem {

    public MaurisMusicDisc(MaurisFolder folder, String name, MaurisTextures textures, ItemCharacteristics itemCharacteristics, boolean generateModel, String model, MaurisBlock maurisBlock, File file, String music, String displayMusicName) {
        super(folder, name, textures, itemCharacteristics, generateModel, model, maurisBlock, file);

        this.music = music;
        this.displayMusicName = displayMusicName;
    }

    @Getter
    String music;
    @Getter
    String displayMusicName;


    public static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){
        ConfigurationSection sec = cs.getConfigurationSection("musicdisc");
        if(sec == null) return;
        mb.setMusicDisc(true);
        String music = sec.getString("music");
        String displayname = sec.getString("displayname");

        mb.setMusic(music);
        mb.setMusicDisplayName(displayname);
    }

}
