package com.ruverq.mauris.items.blocks.blocksounds;


import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.blocks.blockhardness.BlockHardnessHitEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;

import java.util.*;

public class HitListener implements Listener {

    List<Block> blocksHitting = new ArrayList<>();
    HashMap<Block, Integer> blockIntegerHashMap = new HashMap<>();

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        blocksHitting.remove(e.getBlock());
        blockIntegerHashMap.remove(e.getBlock());
    }

    @EventHandler
    public void onAnimation(PlayerAnimationEvent e){
        Set<Material> transparentBlocks = new HashSet<>();
        transparentBlocks.add(Material.WATER);
        transparentBlocks.add(Material.AIR);
        transparentBlocks.add(Material.SNOW);

        Block block = e.getPlayer().getTargetBlock(transparentBlocks, 5);
        if(!blocksHitting.contains(block)) return;
        int damage = addDamage(block);
        if(damage % 5 != 0) return;

        Location loc = block.getLocation();
        World world = block.getWorld();

        world.playSound(loc, "replace.block.wood.hit", SoundCategory.PLAYERS, 0.2f, 0.83f);

    }

    private int addDamage(Block block){
        Object bruh = blockIntegerHashMap.get(block);
        if(bruh == null) return -1;
        int damage = (int) bruh;
        damage++;
        blockIntegerHashMap.remove(block);
        blockIntegerHashMap.put(block, damage);

        return damage;
    }

    public int getDamage(Block block){
        return blockIntegerHashMap.get(block);
    }

    @EventHandler
    public void onDamage(BlockDamageEvent e){
        if(e.getBlock().getBlockData().getSoundGroup().getHitSound() != Sound.BLOCK_WOOD_HIT) return;

        MaurisBlock mb = ItemsLoader.getMaurisBlock(e.getBlock().getBlockData());
        if(mb != null && mb.getSounds().getHitSound() != null) return;

        blocksHitting.add(e.getBlock());
        blockIntegerHashMap.put(e.getBlock(), 0);
    }

    @EventHandler
    public void onHit(BlockHardnessHitEvent e){
        if(e.getBrokenBlock().getDamage() % 4 != 0) return;

        e.getMaurisBlock().getSounds().executeHitSound(e.getBlock().getLocation(), e.getPlayer());
    }

}
