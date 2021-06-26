package com.ruverq.mauris.items.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBlock;
import org.bukkit.GameMode;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MaurisBlockBreak implements Listener {

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
