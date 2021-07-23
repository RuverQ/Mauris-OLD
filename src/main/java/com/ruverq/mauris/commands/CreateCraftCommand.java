package com.ruverq.mauris.commands;

import com.ruverq.mauris.crafts.CraftingManager;
import com.ruverq.mauris.crafts.gui.GUICraftingBench;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCraftCommand implements SimpleCommand{


    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public String name() {
        return "createcraft";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(args.length < 2){
            sender.sendMessage(CommandManager.getPrefix() + "Please provide a name for the craft");
            return;
        }
        Player p = (Player) sender;

        String name = args[1];

        if(CraftingManager.isLoaded(name)){
            sender.sendMessage(CommandManager.getPrefix() + "Craft under this name already exists");
            return;
        }
        GUICraftingBench guiCraftingBench = new GUICraftingBench();
        guiCraftingBench.setName(name.toLowerCase());
        guiCraftingBench.setDefaultSettings();

        guiCraftingBench.setFor(p);

        guiCraftingBench.createAndOpen();
    }
}
