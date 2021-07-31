package com.ruverq.mauris;

import com.ruverq.mauris.commands.CommandManager;
import com.ruverq.mauris.compatibility.MythicMobsLoot;
import com.ruverq.mauris.crafts.CraftingManager;
import com.ruverq.mauris.crafts.listeners.CraftCancel;
import com.ruverq.mauris.guibase.GUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.hud.PlayerHUDInfoManager;
import com.ruverq.mauris.items.icons.MaurisIconPlaceholder;
import com.ruverq.mauris.items.blocks.blockhardness.BlockCBBListener;
import com.ruverq.mauris.items.blocks.blocksounds.MaurisSoundsManager;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import com.ruverq.mauris.items.blocks.listeners.MaurisBlockBreak;
import com.ruverq.mauris.items.blocks.listeners.MaurisBlockCancel;
import com.ruverq.mauris.items.blocks.listeners.MaurisBlockPlace;
import lombok.Setter;
import org.bukkit.Bukkit;
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

        PlayerHUDInfoManager infoManager = new PlayerHUDInfoManager();
        PlayerHUDInfoManager.setUp();
        Bukkit.getPluginManager().registerEvents(infoManager, this);

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null){
            MaurisIconPlaceholder.loadAll();
        }

        Bukkit.getPluginManager().registerEvents(new CraftCancel(), this);
        CraftingManager.setUp(false);


        // Compatibility with other plugins
        if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null){
            Bukkit.getPluginManager().registerEvents(new MythicMobsLoot(), this);
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

        CraftingManager.unloadCrafts();

        if(ResourcePackHelper.server != null){
            ResourcePackHelper.server.stop(0);
        }

    }


}
