package com.ruverq.mauris.items.blocktypes;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaurisNoteBlock implements MaurisBlockType{

    boolean enabled;

    @Override
    public HashMap<String, List<String>> possibleProperties() {
        List<String> pInstruments = new ArrayList<>();
        pInstruments.add("harp");
        pInstruments.add("banjo");
        pInstruments.add("basedrum");
        pInstruments.add("bass");
        pInstruments.add("bell");
        pInstruments.add("bit");
        pInstruments.add("chime");
        pInstruments.add("cow_bell");
        pInstruments.add("didgeridoo");
        pInstruments.add("flute");
        pInstruments.add("guitar");
        pInstruments.add("hat");
        pInstruments.add("iron_xylophone");
        pInstruments.add("pling");
        pInstruments.add("snare");
        pInstruments.add("xylophone");

        List<String> possibleNotes = new ArrayList<>();
        for(int i = 0 ; i < 24; i++){
            possibleNotes.add(String.valueOf(i));
        }

        List<String> pPowered = new ArrayList<>();
        pPowered.add("false");
        pPowered.add("true");

        HashMap<String, List<String>> hashMap = new HashMap<>();
        hashMap.put("instrument", pInstruments);
        hashMap.put("note", possibleNotes);
        hashMap.put("powered", pPowered);

        return hashMap;
    }

    @Override
    public Material material() {
        return Material.NOTE_BLOCK;
    }

}
