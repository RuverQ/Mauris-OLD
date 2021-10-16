package com.ruverq.mauris.items.blocks.listeners;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.blocks.blocktypes.MaurisBlockTypeManager;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MaurisMiddleClickOnBlock implements Listener {

    public void setTimer(){

        new BukkitRunnable() {
            @Override
            public void run() {


                for(Player p : Bukkit.getOnlinePlayers()){
                    if(p.getGameMode() != GameMode.CREATIVE) continue;

                    if(p.getGameMode() != GameMode.CREATIVE) return;

                    ItemStack heldItem = p.getInventory().getItemInMainHand();
                    if(!MaurisBlockTypeManager.isEnabled(heldItem.getType().name())) return;

                    Block b = p.getTargetBlockExact(10, FluidCollisionMode.NEVER);
                    BlockData bd = b.getBlockData();

                    MaurisBlock mb = ItemsLoader.getMaurisBlock(bd);

                    if(mb == null) return;


                    p.getInventory().setItemInMainHand(mb.getAsItemStack());
                    p.updateInventory();

                }

            }
        }.runTaskTimer(Mauris.getInstance(), 20, 5);

    }



}
