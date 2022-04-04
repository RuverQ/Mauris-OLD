package com.ruverq.mauris.items.decorations.listeners;

import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.decorations.MaurisDecoration;
import com.ruverq.mauris.utils.DataContainerUtil;
import com.ruverq.mauris.utils.EntityDataContainerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DecorationBreak implements Listener {

    //Make thats a marker
    @EventHandler
    public void onBreak(EntityDamageEvent e){
        Entity entity = e.getEntity();
        if(!(entity instanceof ArmorStand stand)) return;

        String key = EntityDataContainerUtil.getData(stand, "key");
        if(key == null || key.isEmpty()) return;

        e.setCancelled(true);
        MaurisDecoration item = (MaurisDecoration) ItemsLoader.getMaurisItem(key);
        drop(item, stand.getLocation(), new ItemStack(Material.AIR));
        stand.remove();

        item.getInfo().getSounds().executeBreakSound(stand.getLocation());
    }

    @EventHandler
    public void onBarrierBreak(PlayerInteractEvent e){
        if(e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block b = e.getClickedBlock();
        if(b.getType() != Material.BARRIER) return;

        String value = DataContainerUtil.getData(b, "key");
        if(value == null) return;
        MaurisDecoration dec = (MaurisDecoration) ItemsLoader.getMaurisItem(value);
        b.setType(Material.AIR);
        b.getLocation().getWorld().getNearbyEntities(b.getLocation().add(0.5,0,0.5), 0.5, 1, 0.5).forEach(en ->{
            if(en.getType() == EntityType.ARMOR_STAND && EntityDataContainerUtil.getData(en, "key").equals(value)){
                en.remove();
            }
        });

        drop(dec, b.getLocation(), e.getItem());
        dec.getInfo().getSounds().executeBreakSound(b.getLocation());
    }

    public static boolean drop(MaurisDecoration dec, Location location, ItemStack item){

        if(dec.getInfo().isSelfDrop()){
            location.getWorld().dropItemNaturally(location, dec.getAsItemStack());
        }

        if(dec.getInfo().getLootTable() == null) return true;

        dec.getInfo().getLootTable().dropAll(location, item);
        return true;
    }


}
