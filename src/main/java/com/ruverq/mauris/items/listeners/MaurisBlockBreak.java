package com.ruverq.mauris.items.listeners;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBlock;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class MaurisBlockBreak implements Listener {

    @EventHandler
    public void onIgnite(BlockIgniteEvent e){
        if(e.getCause() == BlockIgniteEvent.IgniteCause.EXPLOSION) return;
        if(e.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL) return;
        if(e.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) return;

        Block blockUnder = e.getBlock().getRelative(BlockFace.DOWN);

        MaurisBlock mb = ItemsLoader.getMaurisBlock(blockUnder.getBlockData());
        if(mb != null) e.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){

        List<Block> blocksToRemove = new ArrayList<>();
        for(Block block : e.blockList()){
            MaurisBlock mb = ItemsLoader.getMaurisBlock(block.getBlockData());
            if(mb != null) {
                blocksToRemove.add(block);
                block.setType(Material.AIR);

                if(mb.isSelfDrop()){
                    block.getLocation().getWorld().dropItemNaturally(block.getLocation(), mb.getAsItemStack());
                }

                if(mb.getLootTable() == null) continue;

                mb.getLootTable().dropAll(block.getLocation());
            }
        }

        e.blockList().removeAll(blocksToRemove);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e){

        List<Block> blocksToRemove = new ArrayList<>();
        for(Block block : e.blockList()){
            MaurisBlock mb = ItemsLoader.getMaurisBlock(block.getBlockData());
            if(mb != null) {
                blocksToRemove.add(block);
                block.setType(Material.AIR);

                if(mb.isSelfDrop()){
                    block.getLocation().getWorld().dropItemNaturally(block.getLocation(), mb.getAsItemStack());
                }

                if(mb.getLootTable() == null) continue;

                mb.getLootTable().dropAll(block.getLocation());
            }
        }

        e.blockList().removeAll(blocksToRemove);

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        BlockData bd = e.getBlock().getBlockData();
        MaurisBlock mb = ItemsLoader.getMaurisBlock(bd);

        if(mb == null) return;
        e.setDropItems(false);

        if(mb.isSelfDrop()){
            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), mb.getAsItemStack());
        }

        if(mb.getLootTable() == null) return;

        mb.getLootTable().dropAll(e.getBlock().getLocation());

    }

}
