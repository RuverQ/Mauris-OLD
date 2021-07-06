package com.ruverq.mauris;

import com.ruverq.mauris.commands.CommandManager;
import com.ruverq.mauris.gui.GUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.icons.MaurisIconPlaceholder;
import com.ruverq.mauris.items.blocks.blockhardness.BlockCBBListener;
import com.ruverq.mauris.items.blocks.blocksounds.MaurisSoundsManager;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import com.ruverq.mauris.items.blocks.listeners.MaurisBlockBreak;
import com.ruverq.mauris.items.blocks.listeners.MaurisBlockCancel;
import com.ruverq.mauris.items.blocks.listeners.MaurisBlockPlace;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Mauris extends JavaPlugin {

    @Setter
    private static Mauris instance;

    static File config;

    @Override
    public void onEnable() {
        //TODO Reformat this below pls

        setInstance(this);

        System.out.println("ooga booga " + Material.DIRT.createBlockData().getSoundGroup().getPlaceSound().toString());

        config = setupConfig();
        reloadConfig();

        MaurisBlockTypeManager.setUp();

        DataHelper.setUp();
        ItemsLoader.load();

        CommandManager.setUp();

        MaurisSoundsManager.setUp();

        ResourcePackHelper.setupRP();

        Bukkit.getPluginManager().registerEvents(new ResourcePackHelper(), this);

        Bukkit.getPluginManager().registerEvents(new GUI(), this);

        Bukkit.getPluginManager().registerEvents(new MaurisBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new MaurisBlockCancel(), this);
        Bukkit.getPluginManager().registerEvents(new MaurisBlockBreak(), this);

        Bukkit.getPluginManager().registerEvents(new ResourcePackHelper(), this);

        Bukkit.getPluginManager().registerEvents(new BlockCBBListener(), this);

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null){
            MaurisIconPlaceholder.loadAll();
        }

    }

    public static Mauris getInstance() {
        return instance;
    }

    private File setupConfig() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info("Creating config file...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        return config;
    }

    @Override
    public void onDisable() {

        if(ResourcePackHelper.server != null){
            ResourcePackHelper.server.stop(0);
        }

    }


}
