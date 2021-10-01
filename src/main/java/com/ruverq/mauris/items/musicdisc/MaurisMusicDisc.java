package com.ruverq.mauris.items.musicdisc;

import com.ruverq.mauris.items.MaurisBuilder;
import com.ruverq.mauris.items.MaurisFolder;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.items.MaurisTextures;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;

public class MaurisMusicDisc extends MaurisItem {

    public MaurisMusicDisc(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, String model, boolean isBlock, MaurisBlock maurisBlock, File file, String music, String displayMusicName) {
        super(folder, name, textures, displayName, lore, material, generateModel, model, isBlock, maurisBlock, file);

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
