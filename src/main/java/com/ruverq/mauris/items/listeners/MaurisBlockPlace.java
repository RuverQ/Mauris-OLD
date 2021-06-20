package com.ruverq.mauris.items.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.utils.PlayerAnimation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaurisBlockPlace implements Listener {



    @EventHandler
    public void onPlace(PlayerInteractEvent e){

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemInteract = e.getItem();
        if(itemInteract == null) return;
        itemInteract.setAmount(1);

        Block newBlock = getBlockPlaceLocation(e.getPlayer());
        if(newBlock == null) return;
        MaurisItem item = ItemsLoader.getMaurisItem(itemInteract);
        e.setCancelled(true);
        if(item == null || !item.isBlock()) {
            if(!itemInteract.getType().isBlock()) return;
            newBlock.setBlockData(itemInteract.getType().createBlockData());
        }else{
            BlockData bd = item.getAsMaurisBlock().getAsBlockData();
            newBlock.setBlockData(bd);
        }

        PlayerAnimation.play(e.getPlayer());

    }

    public Block getBlockPlaceLocation(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        BlockFace blockFace = targetBlock.getFace(adjacentBlock);
        return targetBlock.getRelative(blockFace);
    }

}
