package com.ruverq.mauris.items.blockhardness;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BrokenBlocksService {

    @Getter
    private static Map<Location, BrokenBlock> brokenBlocks = new HashMap<>();

    public static void createBrokenBlock(Block block) {
        createBrokenBlock(block, -1);
    }

    public static void createBrokenBlock(Block block, int time) {
        if (isBrokenBlock(block.getLocation())) return;
        BrokenBlock brokenBlock;
        if (time == -1) brokenBlock = new BrokenBlock(block);
        else brokenBlock = new BrokenBlock(time, block);
        brokenBlocks.put(block.getLocation(), brokenBlock);
    }

    public static void removeBrokenBlock(Location location) {
        brokenBlocks.remove(location);
    }

    public static BrokenBlock getBrokenBlock(Location location) {
        createBrokenBlock(location.getBlock());
        return brokenBlocks.get(location);
    }

    public static boolean isBrokenBlock(Location location) {
        return brokenBlocks.containsKey(location);
    }


}
