package com.ruverq.mauris.items.icons;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBuilder;
import com.ruverq.mauris.items.MaurisItem;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MaurisIconPlaceholder extends PlaceholderExpansion {

    static boolean enabled;

    static MaurisIconPlaceholder current;

    static HashMap<String, MaurisIcon> icons = new HashMap<>();

    public static void loadAll() {
        icons.clear();

        ConfigurationSection config = Mauris.getInstance().getConfig();

        enabled = config.getBoolean("icons.placeholderSupport");
        if (!enabled) return;

        for (MaurisItem mi : ItemsLoader.getItemsLoaded()) {
            if (!(mi instanceof MaurisIcon)) continue;
            MaurisIcon ic = (MaurisIcon) mi;
            icons.put(ic.getName(), ic);
        }

        MaurisBuilder builder = new MaurisBuilder();

        if (current != null) current.unregister();
        current = new MaurisIconPlaceholder();
        current.register();
    }

    @Override
    public String getIdentifier() {
        return "maurisicon";
    }

    @Override
    public String getAuthor() {
        return "RuverQ";
    }

    @Override
    public String getVersion() {
        return Mauris.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        MaurisIcon icon = icons.get(identifier);
        if (icon == null) return "MaurisError";
        String hexSymbol = String.format("%04x", (int) icon.getSymbol());
        String symbol = "\\u" + hexSymbol;

        return StringEscapeUtils.unescapeJava(symbol);
    }
}
