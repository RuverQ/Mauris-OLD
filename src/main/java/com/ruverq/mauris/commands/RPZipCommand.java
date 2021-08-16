package com.ruverq.mauris.commands;

import com.ruverq.mauris.ResourcePackHelper;
import org.bukkit.command.CommandSender;

public class RPZipCommand implements SimpleCommand{

    @Override
    public String name() {
        return "zip";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        ResourcePackHelper.setupRP();
        sender.sendMessage(CommandManager.getPrefix() + " Zipped and selfhosted!");

    }
}
