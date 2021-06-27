package com.ruverq.mauris.items.blocktypes;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaurisBlockTypeManager {

    private static List<MaurisBlockType> types = new ArrayList<>();
    private static HashMap<String, MaurisBlockType> typesByName = new HashMap<>();
    private static HashMap<Material, MaurisBlockType> typesByMaterial = new HashMap<>();

    private static HashMap<MaurisBlockType, Boolean> typesEnables = new HashMap<>();

    public static void setUp(){
        addType(new MaurisNoteBlock());
        addType(new MaurisMushroomStem());
        addType(new MaurisTripwire());
    }

    public static boolean isEnabled(MaurisBlockType type){
        if(type == null) return false;
        return typesEnables.get(type);
    }

    public static boolean isEnabled(String name){
        return isEnabled(getType(name, false, false));
    }

    private static void addType(MaurisBlockType type){
        Material material = type.material();
        String name = material.name().toLowerCase();

        types.add(type);
        typesByName.put(name, type);
        typesByMaterial.put(material, type);
        typesEnables.put(type, false);
    }

    public static MaurisBlockType getType(Material material){
        return typesByMaterial.get(material);
    }

    public static void enable(MaurisBlockType type){
        if(!typesEnables.containsKey(type)) return;
        typesEnables.remove(type);
        typesEnables.put(type, true);
    }

    public static MaurisBlockType getType(String name){
        return getType(name, true, true);
    }

    public static MaurisBlockType getType(String name, boolean defaultIfNull, boolean enable){
        name = name.toLowerCase();

        MaurisBlockType bt = typesByName.get(name);
        if(bt != null){
            if(enable) enable(bt);
            return bt;
        }

        if(!defaultIfNull) return null;
        MaurisBlockType defaultType = getType("NOTE_BLOCK");

        enable(defaultType);
        return defaultType;
    }

}
