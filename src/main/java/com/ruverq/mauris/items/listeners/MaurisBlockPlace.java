package com.ruverq.mauris.items.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.utils.PlayerAnimation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class MaurisBlockPlace implements Listener {

    @EventHandler
    public void onPlace(PlayerInteractEvent e){

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemInteract = e.getItem();
        if(itemInteract == null) return;
        itemInteract.setAmount(1);


        Block newBlock = getBlockPlaceLocation(e.getPlayer());
        if(newBlock == null) return;

        if(e.getClickedBlock() == null) return;

        if(isOnCooldown(e.getPlayer())) return;

        if(isStandingInside(e.getPlayer(), newBlock)) return;
        MaurisItem item = ItemsLoader.getMaurisItem(itemInteract);

        if(ItemsLoader.getMaurisBlock(e.getClickedBlock().getBlockData()) == null && item == null) return;

        if(item == null || !item.isBlock()) {
            if(!itemInteract.getType().isBlock()) return;
            newBlock.setBlockData(itemInteract.getType().createBlockData());

            newBlock.getLocation().getWorld().playSound(newBlock.getLocation(), itemInteract.getType().createBlockData().getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, 1);
        }else{
            BlockData bd = item.getAsMaurisBlock().getAsBlockData();
            newBlock.setBlockData(bd);

            newBlock.getLocation().getWorld().playSound(newBlock.getLocation(), bd.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, 1);
        }

        setCooldown(e.getPlayer());

        PlayerAnimation.play(e.getPlayer());
    }

    private boolean isStandingInside(Player player, Block block) {
        Location playerLocation = player.getLocation();
        Location blockLocation = block.getLocation();
        return playerLocation.getBlockX() == blockLocation.getBlockX()
                && (playerLocation.getBlockY() == blockLocation.getBlockY()
                || playerLocation.getBlockY() + 1 == blockLocation.getBlockY())
                && playerLocation.getBlockZ() == blockLocation.getBlockZ();
    }

    public Block getBlockPlaceLocation(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        BlockFace blockFace = targetBlock.getFace(adjacentBlock);
        return targetBlock.getRelative(blockFace);
    }


    //For very strange double placing bug
    static HashMap<Player, Long> playerCooldownBlock = new HashMap<>();
    static int cooldown = 2;

    private boolean isOnCooldown(Player player){
        Object get = playerCooldownBlock.get(player);
        if(get == null) return false;
        long a = (long) get;

        long timePassed = System.currentTimeMillis() - a;
        return timePassed <= cooldown;
    }

    private void setCooldown(Player player){
        Object get = playerCooldownBlock.get(player);
        if(get != null) playerCooldownBlock.remove(player);

        playerCooldownBlock.put(player, System.currentTimeMillis());
    }


}
