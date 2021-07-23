package com.ruverq.mauris.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandManager implements TabExecutor {

    public static void setUp(){
        Bukkit.getPluginCommand(getMain()).setExecutor(new CommandManager());

        addCommand(new RPZipCommand());
        addCommand(new SeeItemsCommand());
        addCommand(new ReloadCommand());
        addCommand(new InfoCommand());
        addCommand(new BlockDataListCommand());
        addCommand(new SetHUDFrameCommand());
        addCommand(new CreateCraftCommand());
    }

    public static String getMain() {
        return "mauris";
    }
    public static String getPrefix(){
        return format("#b45ee6Mauris } #cfa0eb");
    }

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static String format(String msg){
        Matcher matcher = pattern.matcher(msg);
        while(matcher.find()){
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, String.valueOf(ChatColor.of(color)));
            matcher = pattern.matcher(msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private static final List<SimpleCommand> simpleCommands = new ArrayList<>();
    private static final HashMap<String, SimpleCommand> shortcutCommand = new HashMap<>();
    private static void addCommand(SimpleCommand sc){
        simpleCommands.add(sc);
        shortcutCommand.put(sc.name().toLowerCase(), sc);
    }

    private static SimpleCommand getCommand(String s){
        return shortcutCommand.get(s.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length < 1){
            sender.sendMessage(getPrefix() + "Недостаточно аргументов");
            return true;
        }

        String sSubCommand = args[0];
        SimpleCommand sc = getCommand(sSubCommand);

        if(sc == null){
            sender.sendMessage(getPrefix() + "Ошибка");
            return true;
        }
        if(sc.onlyPlayer() && !(sender instanceof Player)){
            sender.sendMessage(getPrefix() + "Эту команду нельзя возпроизвести из консоли");
            return true;
        }
        if(!sender.hasPermission(sc.permission())){
            sender.sendMessage(getPrefix() + "Нет прав");
            return true;
        }

        sc.execute(sender, getPrefix(), args);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length < 2) {
            List<String> commands = new ArrayList<>();
            for (SimpleCommand scommand : simpleCommands) {
                commands.add(scommand.name());
            }
            return commands;
        }else{

            SimpleCommand scS = getCommand(args[0]);
            if(scS == null) return new ArrayList<>();

            return scS.tabCompleter(args);
        }
    }
}
