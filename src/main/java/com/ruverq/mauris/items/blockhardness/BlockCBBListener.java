package com.ruverq.mauris.items.blockhardness;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisBlock;
import com.ruverq.mauris.items.blocksounds.HitListener;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

//From this https://www.spigotmc.org/threads/1-8-1-13-custom-block-breaking-change-block-hardness.362586/
public class BlockCBBListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!BrokenBlocksService.isBrokenBlock(e.getClickedBlock().getLocation())) return;
        BrokenBlock brokenBlock = BrokenBlocksService.getBrokenBlock(e.getClickedBlock().getLocation());
        brokenBlock.destroyBlockObject();
    }

    @EventHandler
    public void onAnimation(PlayerAnimationEvent e){

        EntityPlayer entityplayer = ((CraftPlayer) e.getPlayer()).getHandle();
        Set<Material> transparentBlocks = new HashSet<>();
        transparentBlocks.add(Material.WATER);
        transparentBlocks.add(Material.AIR);
        transparentBlocks.add(Material.SNOW);
        Block block = entityplayer.getBukkitEntity().getTargetBlock(transparentBlocks, 5);
        Location blockPosition = block.getLocation();

        if (!BrokenBlocksService.isBrokenBlock(blockPosition)) return;

        Player player = entityplayer.getBukkitEntity();

        double distanceX = blockPosition.getX() - entityplayer.locX();
        double distanceY = blockPosition.getY() - entityplayer.locY();
        double distanceZ = blockPosition.getZ() - entityplayer.locZ();

        if (distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ >= 1024.0D) return;
        BrokenBlock brokenBlock = BrokenBlocksService.getBrokenBlock(blockPosition);

        BlockHardnessHitEvent hitEvent = new BlockHardnessHitEvent(player, block, brokenBlock);

        Bukkit.getPluginManager().callEvent(hitEvent);

        brokenBlock.incrementDamage(player, 1);
    }

    @EventHandler
    public void onDamage(BlockDamageEvent e){
        MaurisBlock mb = ItemsLoader.getMaurisBlock(e.getBlock().getBlockData());
        removeSlowDig(e.getPlayer());
        BrokenBlocksService.removeBrokenBlock(e.getBlock().getLocation());
        if(mb == null) {
            return;
        }

        ItemStack item = e.getItemInHand().clone();
        item.setAmount(1);

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta instanceof Damageable){
            ((Damageable)itemMeta).setDamage(0);
        }
        item.setItemMeta(itemMeta);

        int hardness = mb.getHardnessFromTool(item);

        addSlowDig(e.getPlayer(), 2000000);
        BrokenBlocksService.createBrokenBlock(e.getBlock(), hardness);
    }

    public void addSlowDig(Player player, int duration) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, -1, false, false), true);
    }

    public void removeSlowDig(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
    }
}
