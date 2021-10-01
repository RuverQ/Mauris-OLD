package com.ruverq.mauris.items.musicdisc.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.items.musicdisc.MaurisMusicDisc;
import com.ruverq.mauris.utils.DataContainerUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class MusicDiscListener implements Listener {

    @EventHandler
    public void onJukeboxBreak(BlockBreakEvent e){
        Block cblock = e.getBlock();
        if(cblock.getType() != Material.JUKEBOX) return;

        if(DataContainerUtil.hasData(cblock, "mauris_musicdisc")) {
            String discName = DataContainerUtil.getData(cblock, "mauris_musicdisc");
            MaurisMusicDisc discMauris = (MaurisMusicDisc) ItemsLoader.getMaurisItem(discName);
            Location ejectLocation = cblock.getLocation().add(0.5,1,0.5);
            ejectLocation.getWorld().dropItemNaturally(ejectLocation, discMauris.getAsItemStack());
            DataContainerUtil.clearData(cblock);
            for(Entity entity : ejectLocation.getWorld().getNearbyEntities(ejectLocation, 15, 15, 15)){
                if(!(entity instanceof Player)) return;
                Player p = (Player) entity;
                p.stopSound(discMauris.getMusic(), SoundCategory.RECORDS);
            }
            return;
        }
    }

    @EventHandler
    public void onDiscInsert(PlayerInteractEvent e){
        Action action = e.getAction();
        if(action != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;

        Block cblock = e.getClickedBlock();
        if(cblock.getType() != Material.JUKEBOX) return;
        Jukebox jukebox = (Jukebox) cblock.getState();

        if(jukebox.getPlaying() != Material.AIR){
            return;
        }else if(DataContainerUtil.hasData(cblock, "mauris_musicdisc")) {
            String discName = DataContainerUtil.getData(cblock, "mauris_musicdisc");
            MaurisMusicDisc discMauris = (MaurisMusicDisc) ItemsLoader.getMaurisItem(discName);
            Location ejectLocation = jukebox.getLocation().add(0.5,1,0.5);
            ejectLocation.getWorld().dropItemNaturally(ejectLocation, discMauris.getAsItemStack());
            DataContainerUtil.clearData(cblock);
            for(Entity entity : ejectLocation.getWorld().getNearbyEntities(ejectLocation, 15, 15, 15)){
                if(!(entity instanceof Player)) return;
                Player p = (Player) entity;
                p.stopSound(discMauris.getMusic(), SoundCategory.RECORDS);
            }
            return;
        }

        ItemStack item = e.getItem();
        MaurisItem mitem = ItemsLoader.getMaurisItem(item);
        if(!(mitem instanceof MaurisMusicDisc)) return;
        e.setCancelled(true);
        MaurisMusicDisc musicDisc = (MaurisMusicDisc) mitem;
        DataContainerUtil.setData(cblock, "mauris_musicdisc", musicDisc.getFullName());

        item.setAmount(item.getAmount() - 1);
        e.getPlayer().updateInventory();
        Location playingLocation = jukebox.getLocation().add(0.5,0.5,0.5);

        World world = playingLocation.getWorld();
        world.playSound(playingLocation, musicDisc.getMusic(), SoundCategory.RECORDS, 3.0f, 1.0f);
    }


}
