package com.ruverq.mauris.items.blocks.blocktypes;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class MaurisMushroomStem implements MaurisBlockType{

    @Override
    public HashMap<String, List<String>> possibleProperties() {

        HashMap<String, List<String>> hashMap = new HashMap<>();
        hashMap.put("down", getListOfBooleans());
        hashMap.put("east", getListOfBooleans());
        hashMap.put("north", getListOfBooleans());
        hashMap.put("south", getListOfBooleans());
        hashMap.put("up", getListOfBooleans());
        hashMap.put("west", getListOfBooleans());

        return hashMap;
    }

    @Override
    public Material material() {
        return Material.MUSHROOM_STEM;
    }
}
