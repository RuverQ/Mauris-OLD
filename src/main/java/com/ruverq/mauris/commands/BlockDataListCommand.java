package com.ruverq.mauris.commands;

import com.ruverq.mauris.items.ItemsLoader;
import org.bukkit.command.CommandSender;

import static com.ruverq.mauris.commands.CommandManager.format;

public class BlockDataListCommand implements SimpleCommand{
    @Override
    public String name() {
        return "blockdatalist";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        ItemsLoader.getBlockByBlockData().forEach((bd, mb) ->{

            String remove = "minecraft:";
            String bds = mb.getAsBlockData().getAsString().replace(remove, "");

            sender.sendMessage(format("#969696" + mb.getFolder().getName() + ":" + mb.getName() + " --> " + bds));
        });

    }
}
