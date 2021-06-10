package com.ruverq.mauris;

import com.ruverq.mauris.commands.CommandManager;
import com.ruverq.mauris.items.ItemsLoader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mauris extends JavaPlugin {

    @Getter
    @Setter
    public static Mauris instance;

    @Override
    public void onEnable() {

        setInstance(this);

        DataHelper.setUp();
        ItemsLoader.load();

        CommandManager.setUp();

    }

    @Override
    public void onDisable() {

        if(ResourcePackHelper.server != null){
            ResourcePackHelper.server.stop(0);
        }

    }


}
