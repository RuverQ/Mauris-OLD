package com.ruverq.mauris.commands;

import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.ResourcePackHelper;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocktypes.MaurisBlockTypeManager;
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

        ResourcePackHelper.setupRP();

        sender.sendMessage(CommandManager.getPrefix() + " Reloaded config!");
    }
}
