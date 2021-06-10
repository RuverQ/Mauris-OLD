package com.ruverq.mauris.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public interface SimpleCommand {

    default boolean onlyPlayer() {
        return false;
    }


    String name();
    default String permission(){
        return "Mauris." + name();
    }

    void execute(CommandSender sender, String prefix, String[] args);
    default List<String> tabCompleter(String[] args){
        return new ArrayList<>();
    }

}
