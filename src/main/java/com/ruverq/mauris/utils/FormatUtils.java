package com.ruverq.mauris.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static String formatColor(String msg) {
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(msg);
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> formatColorList(List<String> stringList){
        List<String> lore = new ArrayList<>();

        for(String line : stringList){
            lore.add(formatColor(line));
        }

        return lore;
    }
}
