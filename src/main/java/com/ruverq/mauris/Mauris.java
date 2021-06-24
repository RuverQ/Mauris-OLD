package com.ruverq.mauris;

import com.ruverq.mauris.commands.CommandManager;
import com.ruverq.mauris.gui.GUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocktypes.MaurisMushroomStem;
import com.ruverq.mauris.items.listeners.MaurisBlockCancel;
import com.ruverq.mauris.items.listeners.MaurisBlockPlace;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mauris extends JavaPlugin {

    @Setter
    private static Mauris instance;

    @Override
    public void onEnable() {

        setInstance(this);

        DataHelper.setUp();
        ItemsLoader.load();

        CommandManager.setUp();

        ResourcePackHelper.setupRP();

        Bukkit.getPluginManager().registerEvents(new ResourcePackHelper(), this);

        Bukkit.getPluginManager().registerEvents(new GUI(), this);
        Bukkit.getPluginManager().registerEvents(new MaurisBlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new MaurisBlockCancel(), this);

    }

    public static Mauris getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {

        if(ResourcePackHelper.server != null){
            ResourcePackHelper.server.stop(0);
        }

    }


}
