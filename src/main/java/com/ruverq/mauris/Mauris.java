package com.ruverq.mauris;

import com.ruverq.mauris.commands.CommandManager;
import com.ruverq.mauris.gui.GUI;
import com.ruverq.mauris.items.ItemsLoader;
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

        Bukkit.getPluginManager().registerEvents(new GUI(), this);

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
