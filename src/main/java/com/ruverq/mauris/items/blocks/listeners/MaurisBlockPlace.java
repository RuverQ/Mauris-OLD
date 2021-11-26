package com.ruverq.mauris.items.blocks.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.utils.BlockDataCreator;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MaurisBlockPlace implements Listener {

    @EventHandler
    public void onPlace(PlayerInteractEvent e){

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(e.getClickedBlock() == null) return;
        if(isOnCooldown(e.getPlayer())) return;

        EquipmentSlot hand = e.getHand();

        ItemStack itemStack = e.getItem();
        ItemStack itemInteract = itemStack;

        if(itemInteract == null && hand == EquipmentSlot.OFF_HAND) {
            return;
        }

        MaurisItem item = ItemsLoader.getMaurisItem(itemInteract);
        //If block in left hand
        if(itemInteract == null || (item == null && !itemStack.getType().isBlock())){
            itemStack = e.getPlayer().getInventory().getItemInOffHand();
            itemInteract = itemStack;

            hand = EquipmentSlot.OFF_HAND;
        }

        if(itemInteract.getType().isAir()) return;
        if(itemInteract.getType() == Material.NOTE_BLOCK) return;
        itemInteract = itemInteract.clone();
        itemInteract.setAmount(1);

        Block newBlock = getBlockPlaceLocation(e.getPlayer());
        if(newBlock == null) return;

        if(isInside(e.getPlayer(), newBlock)) return;

        item = ItemsLoader.getMaurisItem(itemInteract);
        MaurisBlock mbclick = ItemsLoader.getMaurisBlock(e.getClickedBlock().getBlockData());

        if(mbclick == null && item == null) {
            return;
        }

        if(item == null || !item.isBlock()) {
            if(!itemInteract.getType().isBlock()) return;

            BlockDataCreator.placeBlock(e.getPlayer(), newBlock, itemInteract, hand);
            if(!newBlock.getType().isAir()) newBlock.getLocation().getWorld().playSound(newBlock.getLocation(), itemInteract.getType().createBlockData().getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, 1);
        }else{
            MaurisBlock mb = item.getAsMaurisBlock();
            BlockData bd = mb.getAsBlockData();
            newBlock.setBlockData(bd);

            mb.getSounds().executePlaceSound(newBlock.getLocation());
        }

        if(e.getPlayer().getGameMode() != GameMode.CREATIVE && !newBlock.getType().isAir() && item != null && item.isBlock()){
            itemStack.setAmount(itemStack.getAmount() - 1);
            e.getPlayer().updateInventory();
        }

        setCooldown(e.getPlayer());

        if(hand == EquipmentSlot.HAND) e.getPlayer().swingMainHand();
        if(hand == EquipmentSlot.OFF_HAND) e.getPlayer().swingOffHand();
    }

    private boolean isInside(Player player, Block block) {
        Location playerLoc = player.getLocation();
        Location blockLoc = block.getLocation();
        return playerLoc.getBlockX() == blockLoc.getBlockX() && (playerLoc.getBlockY() == blockLoc.getBlockY() || playerLoc.getBlockY() + 1 == blockLoc.getBlockY()) && playerLoc.getBlockZ() == blockLoc.getBlockZ();
    }

    public BlockFace getBlockPlaceFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 7);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

    private final Set<Material> transparentPlacement = Set.of(Material.WATER, Material.LAVA,
            Material.AIR, Material.CAVE_AIR,
            Material.DEAD_BUSH, Material.WEEPING_VINES, Material.WEEPING_VINES_PLANT,
            Material.GRASS, Material.TALL_GRASS, Material.FERN,
            Material.SEAGRASS, Material.TALL_SEAGRASS,
            Material.SNOW);

    public Block getBlockPlaceLocation(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(transparentPlacement, 7);
        if (lastTwoTargetBlocks.size() != 2) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        BlockFace blockFace = targetBlock.getFace(adjacentBlock);
        return targetBlock.getRelative(blockFace);
    }

    //For very strange double placing bug
    static HashMap<Player, Long> playerCooldownBlock = new HashMap<>();
    static int cooldown = 50;

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
