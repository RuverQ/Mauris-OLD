package com.ruverq.mauris.items.hud;

import com.ruverq.mauris.items.*;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.icons.MaurisIcon;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MaurisHUD extends MaurisItem {

    int xOffset;
    boolean enabled;

    List<String> frames;

    public MaurisHUD(MaurisFolder folder, String name, MaurisTextures textures, String displayName, List<String> lore, Material material, boolean generateModel, String model, boolean isBlock, MaurisBlock maurisBlock, File file, int xOffset, boolean hudEnabled, List<String> frames, boolean vanillaIterator, int vanillaIterate, List<GameModeChecker> gameModeCheckers, boolean undrwtrV) {
        super(folder, name, textures, displayName, lore, material, generateModel, model, isBlock, maurisBlock, file);
        this.xOffset = xOffset;
        this.enabled = hudEnabled;
        this.frames = frames;
        this.vanillaIterator = vanillaIterator;
        this.vanillaIterate = vanillaIterate;
        this.gameModeCheckers = gameModeCheckers;
        this.underwaterVisibility = undrwtrV;
    }

    boolean vanillaIterator;
    int vanillaIterate;


    List<GameModeChecker> gameModeCheckers;

    boolean underwaterVisibility;

    @Override
    public void generateId() { }

    @Override
    public void generate() { }

    MaurisIcon getIcon(int id){
        String iconS = frames.get(id);
        MaurisIcon item = ItemsLoader.getIcons().get(iconS);
        return item;
    }

    public static void loadFromConfigurationSection(MaurisBuilder mb, ConfigurationSection cs){
        ConfigurationSection hudkcs = cs.getConfigurationSection("hud");
        if(hudkcs == null) return;

        int xOffset = hudkcs.getInt("xOffset");
        boolean enabled = hudkcs.getBoolean("enabled");
        List<String> frames = hudkcs.getStringList("frames");

        boolean vanillaIterator = hudkcs.getBoolean("vanillaIterator.enabled", false);
        int vanillaIterate = hudkcs.getInt("vanillaIterator.amount", 0);

        List<GameModeChecker> gameModeCheckers = new ArrayList<>();
        for(GameMode gameMode : GameMode.values()){
            boolean visibility = hudkcs.getBoolean("visibility." + gameMode.name().toLowerCase(), true);
            gameModeCheckers.add(new GameModeChecker(gameMode, visibility));
        }

        boolean underwaterVisibility = hudkcs.getBoolean("visibility.underwater", true);

        mb.setHUD(true)
                .setXOffset(xOffset)
                .setFrames(frames)
                .setHudEnabled(enabled)
                .setVanillaIterate(vanillaIterate)
                .setVanillaIterator(vanillaIterator)
                .setGameModeCheckers(gameModeCheckers)
                .setUnderwaterVisibility(underwaterVisibility);
    }

}
