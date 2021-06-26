package com.ruverq.mauris.items.blocktypes;

import com.ruverq.mauris.utils.BlockProperty;
import com.ruverq.mauris.utils.BlockStateParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.*;

public interface MaurisBlockType {

    HashMap<String, List<String>> possibleProperties();
    Material material();

    static MaurisBlockType getFromName(String name){
        if(name.equalsIgnoreCase("NOTE_BLOCK")) return new MaurisNoteBlock();
        if(name.equalsIgnoreCase("MUSROOMSTEM")) return new MaurisMushroomStem();
        if(name.equalsIgnoreCase("TRIPWIRE")) return new MaurisTripwire();

        return new MaurisNoteBlock();
    }

    default List<BlockProperty> generate(int seed){

        if(seed > getMaxPossibleBlocks()) return null;

        HashMap<String, Integer> valueStorage = new HashMap<>();

        for(String key : possibleProperties().keySet()) {
            List<String> values = possibleProperties().get(key);

            int size = values.size();

            int keyValue;
            int divider = 1;
            for(String keyStorage : valueStorage.keySet()){
                divider = divider * possibleProperties().get(keyStorage).size();
            }
            keyValue = (seed / divider) % size;

            valueStorage.put(key, keyValue);
        }

        List<BlockProperty> properties = new ArrayList<>();
        for(String s : valueStorage.keySet()){
            int inte = valueStorage.get(s);

            BlockProperty property = new BlockProperty();
            property.setProperty(s);
            property.setValue(possibleProperties().get(s).get(inte));

            properties.add(property);
        }

        return properties;
    }

    default List<BlockData> possibleBlocks(){
        List<BlockData> possibleBlocks = new ArrayList<>();

        for(int i = 0 ; i < getMaxPossibleBlocks() ; i++){
            possibleBlocks.add(BlockStateParser.createData(material(), generate(i)));
        }

        return possibleBlocks;
    }

    default List<String> getListOfBooleans(){
        List<String> p = new ArrayList<>();
        p.add("false");
        p.add("true");

        return p;
    }

    default int getMaxPossibleBlocks(){
        int maxPossibleBlocks = 0;

        int beforeValue = -1;
        for(List<String> value : possibleProperties().values()){
            if(beforeValue == -1){
                beforeValue = value.size();
            }else{
                beforeValue = beforeValue * value.size();
            }
        }

        maxPossibleBlocks = beforeValue;

        return maxPossibleBlocks;
    }

}
