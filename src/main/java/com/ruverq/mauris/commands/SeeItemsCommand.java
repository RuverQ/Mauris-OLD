package com.ruverq.mauris.commands;

import com.ruverq.mauris.gui.FoldersGUI;
import com.ruverq.mauris.items.ItemsLoader;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeeItemsCommand implements SimpleCommand{

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public String name() {
        return "see";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        Player player = (Player) sender;

        FoldersGUI foldersGUI = new FoldersGUI();
        foldersGUI.setFor(player);
        foldersGUI.setFolders(ItemsLoader.getLoadedFolders());
        foldersGUI.setDefaultSettings();

        foldersGUI.open();
    }
}