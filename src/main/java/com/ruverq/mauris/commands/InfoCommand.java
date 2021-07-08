package com.ruverq.mauris.commands;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.icons.MaurisIcon;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ruverq.mauris.commands.CommandManager.format;

public class InfoCommand implements SimpleCommand{
    @Override
    public String name() {
        return "info";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {
        if(args.length < 2){
            sender.sendMessage(prefix + "Please provide me name of MaurisItem");
            return;
        }
        String name = args[1];

        MaurisItem item = ItemsLoader.getMaurisItem(name);

        if(item == null){
            sender.sendMessage(prefix + "Can't find this item");
            return;
        }

        String purpleMaurisDark = format("#b45ee6");
        String purpleMauris = format("#cfa0eb");
        String lightBlue = format("#45f9ff");

        sender.sendMessage(purpleMaurisDark + "~~~~~ " + purpleMauris + "MaurisItem " + lightBlue + item.getName() + purpleMaurisDark + " ~~~~~");

        sender.sendMessage(lightBlue + "== General Info");
        sender.sendMessage(purpleMaurisDark + "| Name: " + purpleMauris + item.getName());
        sender.sendMessage(purpleMaurisDark + "| FullName: " + purpleMauris + item.getFolder().getName() + ":" + item.getName());

        if(item instanceof MaurisBlock){
            MaurisBlock mb = (MaurisBlock) item;
            sender.sendMessage(lightBlue + "== Block");
            sender.sendMessage(purpleMaurisDark + "| Type: " + purpleMauris + mb.getType().material().name());
            sender.sendMessage(purpleMaurisDark + "| ItemID " + purpleMauris + mb.getId());
            sender.sendMessage(purpleMaurisDark + "| BlockID " + purpleMauris + mb.getBlockId());
            sender.sendMessage(purpleMaurisDark + "| BlockData: " + purpleMauris + mb.getAsBlockData().getAsString());
        }else if(item instanceof MaurisIcon){
            MaurisIcon icon = (MaurisIcon) item;
            sender.sendMessage(lightBlue + "== Icon");
            sender.sendMessage(purpleMaurisDark + "| Icon: " + purpleMauris + icon.getSymbol());
            sender.sendMessage(purpleMaurisDark + "| IconID: " + purpleMauris + icon.getId());

        }else{
            sender.sendMessage(lightBlue + "== Item");
            sender.sendMessage(purpleMaurisDark + "| Material: " + purpleMauris + item.getMaterial().name());
            sender.sendMessage(purpleMaurisDark + "| ID: " + purpleMauris + item.getId());
        }
    }

    @Override
    public List<String> tabCompleter(String[] args) {
        String name = args[1];

        Set<String> items = ItemsLoader.getItemsByName().keySet();

        List<String> list = items.stream().filter((s) -> s.startsWith(name)).collect(Collectors.toList());

        return list;
    }
}
