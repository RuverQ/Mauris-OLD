package com.ruverq.mauris.crafts.discover;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.crafts.discover.listeners.DLPlayerJoin;
import com.ruverq.mauris.crafts.discover.listeners.DLPlayerPickup;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscoverManager {

    public static List<DiscoverListener> listeners = new ArrayList<>();
    public static HashMap<String, DiscoverListener> listenersByName = new HashMap<>();

    public static void setUp(boolean reload){
        addListener(new DLPlayerJoin(), reload);
        addListener(new DLPlayerPickup(), reload);
    }

    public static void unload(){
        listeners.clear();
        listenersByName.clear();
    }

    private static void addListener(DiscoverListener listener, boolean reload){
        listeners.add(listener);
        listenersByName.put(listener.name().toLowerCase(), listener);
        if(!reload) Bukkit.getPluginManager().registerEvents(listener, Mauris.getInstance());
    }

    public static void addToQueue(String line, NamespacedKey key){
        String[] args = line.split(" ");
        if(args.length == 0) return;

        String name = args[0];
        name = name.toLowerCase();

        DiscoverListener listener = listenersByName.get(name);
        if(listener == null) return;

        listener.addToQueue(line, key);
    }



}
