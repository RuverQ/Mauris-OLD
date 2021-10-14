package com.ruverq.mauris.commands;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveCommand implements SimpleCommand{

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public String name() {
        return "give";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(args.length < 2){
            sender.sendMessage(CommandManager.getPrefix() + "Please provide a name of the MaurisItem");
            return;
        }
        Player p = (Player) sender;
        String name = args[1];

        MaurisItem item = ItemsLoader.getMaurisItem(name);
        p.getInventory().addItem(item.getAsItemStack());
        p.updateInventory();

        sender.sendMessage(CommandManager.getPrefix() + "Successfully given " + item.getName());
    }

    @Override
    public List<String> tabCompleter(String[] args) {
        return ItemsLoader.getItemsByName().keySet().stream().toList();
    }
}
