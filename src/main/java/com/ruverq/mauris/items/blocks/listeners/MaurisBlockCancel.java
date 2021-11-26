package com.ruverq.mauris.items.blocks.listeners;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MaurisBlockCancel implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNote(NotePlayEvent e){
        if(MaurisBlockTypeManager.isEnabled("NOTE_BLOCK")) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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

    @EventHandler(priority = EventPriority.HIGHEST)
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e){
        if(MaurisBlockTypeManager.isEnabled(e.getBlock().getType().name())) e.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent e){

        if(e.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if(e.getClickedBlock() == null) return;
        if(MaurisBlockTypeManager.isEnabled(e.getClickedBlock().getType().name())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPhysics(BlockPhysicsEvent e){

        if(MaurisBlockTypeManager.isEnabled(e.getChangedType().name())){
            e.setCancelled(true);
        }

        if(MaurisBlockTypeManager.isEnabled("TRIPWIRE")){
            updateAndCheckSurroundings(e.getBlock().getLocation(), BlockFace.EAST);
            updateAndCheckSurroundings(e.getBlock().getLocation(), BlockFace.NORTH);
            updateAndCheckSurroundings(e.getBlock().getLocation(), BlockFace.WEST);
            updateAndCheckSurroundings(e.getBlock().getLocation(), BlockFace.SOUTH);
        }

        if(MaurisBlockTypeManager.isEnabled("NOTE_BLOCK")){
            Block blockAbove = e.getBlock().getRelative(BlockFace.UP);

            if(blockAbove.getType() == Material.NOTE_BLOCK){
                updateAndCheck(e.getBlock());
                e.setCancelled(true);

                checkDoor(e.getBlock());
            }
        }
    }

    // Author: MMoneyKiller | https://github.com/MMonkeyKiller/CustomBlocks
    private void checkDoor(Block b){
        Block bottomBlock = b.getRelative(BlockFace.DOWN);
        if (Tag.DOORS.isTagged(b.getType()) && b.getBlockData() instanceof Door data) {
            if (!data.getHalf().equals(Bisected.Half.TOP)) return;
            Door d = (Door) bottomBlock.getBlockData();
            d.setOpen(data.isOpen());
            bottomBlock.setBlockData(d);
            bottomBlock.getState().update(true, false);
        }
    }

    public void updateAndCheckSurroundings(Location location, BlockFace face){
        Block b = location.getBlock().getRelative(face);

        if(b.getType() != Material.TRIPWIRE) return;

        BlockData bd = b.getBlockData();
        new BukkitRunnable() {
            @Override
            public void run() {
                BlockData newbd = b.getBlockData();
                if(newbd.matches(bd)) return;

                b.setBlockData(bd, false);
            }
        }.runTaskLater(Mauris.getInstance(), 0);
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
}
