package com.ruverq.mauris.items.blocks.blocktypes;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaurisTripwire implements MaurisBlockType{

    @Override
    public HashMap<String, List<String>> possibleProperties() {

        HashMap<String, List<String>> hashMap = new HashMap<>();
        hashMap.put("attached", getListOfBooleans());
        hashMap.put("disarmed", getListOfBooleans());
        hashMap.put("east", getListOfBooleans());
        hashMap.put("north", getListOfBooleans());

        List<String> list = new ArrayList<>();
        list.add("true");
        hashMap.put("powered", list);
        //hashMap.put("powered", getListOfBooleans()); temporarily

        hashMap.put("south", getListOfBooleans());
        hashMap.put("west", getListOfBooleans());

        return hashMap;
    }

    @Override
    public Material material() {
        return Material.TRIPWIRE;
    }
}
