package com.ruverq.mauris.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.List;

public class BlockStateParser {

    public static boolean isPropertyTrue(Block block, List<BlockProperty> properties){
        if(properties.isEmpty()) return true;

        BlockData data = block.getBlockData();

        String formattedData = createFormattedData(block.getType(), properties);
        BlockData createdData = Bukkit.createBlockData(formattedData);

        return data.matches(createdData);
    }

    public static BlockData createData(Material material, List<BlockProperty> properties){

        StringBuilder sb = new StringBuilder();
        sb.append(material.getKey().getNamespace()).append(":").append(material.getKey().getKey());
        sb.append("[");

        for(BlockProperty property : properties){
            sb.append(property.getProperty()).append("=").append(property.getValue()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append("]");

        return Bukkit.createBlockData(sb.toString());
    }


    public static String createFormattedData(Material material, List<BlockProperty> properties){

        StringBuilder sb = new StringBuilder();
        sb.append(material.getKey().getNamespace()).append(":").append(material.getKey().getKey());
        sb.append("[");

        for(BlockProperty property : properties){
            sb.append(property.getProperty()).append("=").append(property.getValue()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append("]");

        return sb.toString();
    }
}
