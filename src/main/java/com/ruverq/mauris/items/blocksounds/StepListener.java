package com.ruverq.mauris.items.blocksounds;


import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class StepListener implements Listener {

    static HashMap<Player, Integer> playersInMove = new HashMap<>();

    static int maxMoves = 8;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player pl = e.getPlayer();

        int moves = 0;

        if(pl.isSneaking()){
            return;
        }

        if(pl.isSprinting()){
            moves = addMoveI(pl, 2);
        }else{
            moves = addMoveI(e.getPlayer());
        }

        if(moves > maxMoves){
            playersInMove.remove(e.getPlayer());
        }else{
            return;
        }

        if(pl.getGameMode() == GameMode.SPECTATOR) return;

        if(pl.isInWater()) return;

        Block blockUnder = pl.getLocation().getBlock().getRelative(BlockFace.DOWN);

        Location loc = blockUnder.getLocation();
        World world = blockUnder.getWorld();

        MaurisBlock mBlock = ItemsLoader.getMaurisBlock(blockUnder.getBlockData());

        if(mBlock != null){
            mBlock.getSounds().executeStepSound(loc);
            return;
        }

        if(blockUnder.getBlockData().getSoundGroup().getStepSound() == Sound.BLOCK_WOOD_STEP){
            world.playSound(loc, "replace.block.wood.step", SoundCategory.PLAYERS, 0.2f, 1);
        }
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent e){
        playersInMove.remove(e.getPlayer());
    }

    private static int addMoveI(Player player, int amount){
        playersInMove.putIfAbsent(player, 0);

        int i = playersInMove.get(player);
        i += amount;

        playersInMove.remove(player);
        playersInMove.put(player, i);

        return i;
    }

    private static int addMoveI(Player player){
        playersInMove.putIfAbsent(player, 0);

        int i = playersInMove.get(player);
        i++;

        playersInMove.remove(player);
        playersInMove.put(player, i);

        return i;
    }

    private static int getMoveI(Player player){
        playersInMove.putIfAbsent(player, 0);
        return playersInMove.get(player);
    }



}
