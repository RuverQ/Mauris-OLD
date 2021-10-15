package com.ruverq.mauris.items.blocks.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.blocks.MaurisLootTable;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

                if(!isWorked(mb.getChanceToBeBlownUp())) continue;
                block.setType(Material.AIR);

                drop(block, e.getLocation(), new ItemStack(Material.AIR));
            }else{
                boolean dropped = drop(block, block.getLocation(), new ItemStack(Material.AIR));
                if(dropped){
                    blocksToRemove.add(block);
                    block.setType(Material.AIR);
                }
            }
        }

        e.blockList().removeAll(blocksToRemove);
    }

    private static boolean isWorked(double chance){
        if(chance <= 0) return false;

        double random = new Random().nextDouble();
        return random < chance;
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e){

        List<Block> blocksToRemove = new ArrayList<>();
        for(Block block : e.blockList()){
            MaurisBlock mb = ItemsLoader.getMaurisBlock(block.getBlockData());
            if(mb != null) {
                blocksToRemove.add(block);

                if(!isWorked(mb.getChanceToBeBlownUp())) continue;
                block.setType(Material.AIR);

                drop(block, block.getLocation(), new ItemStack(Material.AIR));
            }else{
                boolean dropped = drop(block, block.getLocation(), new ItemStack(Material.AIR));
                if(dropped){
                    blocksToRemove.add(block);
                    block.setType(Material.AIR);
                }
            }
        }

        e.blockList().removeAll(blocksToRemove);

    }


    public boolean drop(Block b, Location location, ItemStack item){

        BlockData bd = b.getBlockData();
        MaurisBlock mb = ItemsLoader.getMaurisBlock(bd);

        if(mb == null){
            MaurisLootTable mlt = MaurisLootTable.getLootTableByName(b.getType().name());
            if(mlt == null) return false;
            mlt.dropAll(location, item);
            return true;
        }

        if(mb.isSelfDrop()){
            location.getWorld().dropItemNaturally(location, mb.getAsItemStack());
        }

        if(mb.getLootTable() == null) return true;

        mb.getLootTable().dropAll(location, item);
        return true;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        boolean dropped = drop(e.getBlock(), e.getBlock().getLocation(), e.getPlayer().getInventory().getItemInMainHand());
        if(dropped) e.setDropItems(false);
    }

}
