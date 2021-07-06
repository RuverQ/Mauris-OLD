package com.ruverq.mauris.items.blocks.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class MaurisBlockCancel implements Listener {

    @EventHandler
    public void onNote(NotePlayEvent e){
        if(MaurisBlockTypeManager.isEnabled("NOTE_BLOCK")) e.setCancelled(true);
    }

    @EventHandler
    public void onPiston(BlockPistonRetractEvent e){

        int maxPiston = 8;

        BlockFace bf = e.getDirection();
        Block block = e.getBlock();
        for(int i = 0 ; i < maxPiston ; i++){
            block = block.getRelative(bf);
            if(block.getType().isAir()) break;
            if(ItemsLoader.getMaurisBlock(block.getBlockData()) != null) {
                e.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onPiston(BlockPistonExtendEvent e){

        int maxPiston = 8;

        BlockFace bf = e.getDirection();
        Block block = e.getBlock();
        for(int i = 0 ; i < maxPiston ; i++){
            block = block.getRelative(bf);
            if(block.getType().isAir()) break;
            if(ItemsLoader.getMaurisBlock(block.getBlockData()) != null) {
                e.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(MaurisBlockTypeManager.isEnabled(e.getBlock().getType().name())) e.setCancelled(true);
    }


    @EventHandler
    public void onClick(PlayerInteractEvent e){

        if(e.getAction() == Action.LEFT_CLICK_BLOCK) return;

        if(e.getClickedBlock() == null) return;
        if(MaurisBlockTypeManager.isEnabled(e.getClickedBlock().getType().name())) e.setCancelled(true);
    }



    @EventHandler
    public void onPhysics(BlockPhysicsEvent e){
        if(MaurisBlockTypeManager.isEnabled("NOTE_BLOCK")){
            Block blockAbove = e.getBlock().getRelative(BlockFace.UP);

            if(blockAbove.getType() == Material.NOTE_BLOCK){
                updateAndCheck(e.getBlock());
                e.setCancelled(true);
            }
        }else if(MaurisBlockTypeManager.isEnabled(e.getChangedType().name())) {
            e.setCancelled(true);
        }
    }

    // Author: MMoneyKiller | https://www.spigotmc.org/threads/prevent-noteblock-blockstate-updating.483242/
    public void updateAndCheck(Block block) {
        Block b = block.getRelative(BlockFace.UP);
        if (b.getType() == Material.NOTE_BLOCK)
            b.getState().update(true, true);
        Block nextBlock = block.getRelative(BlockFace.UP);
        if (nextBlock.getType() == Material.NOTE_BLOCK)
            updateAndCheck(b);
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

}
