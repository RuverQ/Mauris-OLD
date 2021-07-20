package com.ruverq.mauris.commands;

import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.hud.PlayerHUDInfoManager;
import com.ruverq.mauris.items.icons.MaurisIconPlaceholder;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SimpleCommand{
    @Override
    public String name() {
        return "reload";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        Mauris.getInstance().reloadConfig();

        MaurisBlockTypeManager.setUp();

        DataHelper.setUp();
        ItemsLoader.load();

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null){
            MaurisIconPlaceholder.loadAll();
        }

        PlayerHUDInfoManager.setUp();

        //ResourcePackHelper.setupRP();

        sender.sendMessage(CommandManager.getPrefix() + " Reloaded config!");
    }
}
