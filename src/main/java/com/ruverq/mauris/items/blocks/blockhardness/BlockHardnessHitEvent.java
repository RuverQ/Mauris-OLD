package com.ruverq.mauris.items.blocks.blockhardness;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockHardnessHitEvent extends Event {

    Player player;
    Block block;
    BrokenBlock brokenBlock;

    public BlockHardnessHitEvent(Player player, Block block, BrokenBlock brokenBlock) {
        this.player = player;
        this.block = block;
        this.brokenBlock = brokenBlock;
    }

    private static final HandlerList handlers = new HandlerList();

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public BrokenBlock getBrokenBlock() {
        return brokenBlock;
    }

    public MaurisBlock getMaurisBlock() {
        return ItemsLoader.getMaurisBlock(block.getBlockData());
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
