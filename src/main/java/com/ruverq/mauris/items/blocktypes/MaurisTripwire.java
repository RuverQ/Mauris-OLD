package com.ruverq.mauris.items.blocktypes;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class MaurisTripwire implements MaurisBlockType{

    boolean enabled;

    @Override
    public HashMap<String, List<String>> possibleProperties() {

        HashMap<String, List<String>> hashMap = new HashMap<>();
        hashMap.put("attached", getListOfBooleans());
        hashMap.put("disarmed", getListOfBooleans());
        hashMap.put("east", getListOfBooleans());
        hashMap.put("north", getListOfBooleans());
        hashMap.put("powered", getListOfBooleans());
        hashMap.put("south", getListOfBooleans());
        hashMap.put("west", getListOfBooleans());

        return hashMap;
    }

    @Override
    public Material material() {
        return Material.TRIPWIRE;
    }
}
