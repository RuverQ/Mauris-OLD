package com.ruverq.mauris.utils;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.context.ItemActionContext;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

// Thanks MoneyKiller123 https://www.spigotmc.org/threads/block-placing-facing-an-interactable-block-without-sneaking.492559/
public class BlockDataCreator {

    public static void placeBlock(Player player, Block newBlock, ItemStack itemPlace, EquipmentSlot spigothand){
        EntityHuman entityHuman = ((CraftPlayer)player).getHandle();

        EnumHand hand;

        if(spigothand == EquipmentSlot.HAND){
            hand = EnumHand.a;
        }else{
            hand = EnumHand.b;
        }

        MovingObjectPositionBlock movingObjectPositionBlock = new MovingObjectPositionBlock(LocToVec(player.getEyeLocation()), entityHuman.getDirection(), BlockToBlockPos(newBlock), false);
        ItemActionContext itemActionContext = new ItemActionContext(entityHuman, hand, movingObjectPositionBlock);

        RayTraceResult rtr = newBlock.getWorld().rayTraceBlocks(player.getEyeLocation(),
                player.getLocation().getDirection(), 8, FluidCollisionMode.NEVER, true);

        Location interactionPoint = rtr.getHitPosition().subtract(rtr.getHitBlock().getLocation().toVector())
                .toLocation(player.getWorld());

        if(Tag.SLABS.isTagged(itemPlace.getType())){
            Slab.Type dataType = newBlock.getType() == itemPlace.getType() ? Slab.Type.DOUBLE
                    : (interactionPoint.getY() > 0.0d && interactionPoint.getY() < 0.5d)
                    || interactionPoint.getY() == 1.0d ? Slab.Type.BOTTOM : Slab.Type.TOP;
            if (dataType != Slab.Type.DOUBLE)
                CraftItemStack.asNMSCopy(itemPlace).placeItem(itemActionContext, hand);
            Slab data = (Slab) newBlock.getBlockData();
            data.setType(dataType);
            newBlock.setBlockData(data);
        }else if (Tag.STAIRS.isTagged(itemPlace.getType())) {
            CraftItemStack.asNMSCopy(itemPlace).placeItem(itemActionContext, hand);
            Stairs data = ((Stairs) newBlock.getBlockData());
            data.setHalf((interactionPoint.getY() < 0.5d && interactionPoint.getY() >= 0.0d) ? Bisected.Half.BOTTOM : Bisected.Half.TOP);
            newBlock.setBlockData(data);
        }
        else{
            CraftItemStack.asNMSCopy(itemPlace).placeItem(itemActionContext, hand);
        }

    }

    public static BlockPosition BlockToBlockPos(Block block) {
        return new BlockPosition(block.getX(), block.getY(), block.getZ());
    }

    public static Vec3D LocToVec(Location loc) {
        return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
    }

    public static Location getInteractionPoint(World world, MovingObjectPositionBlock movingobjectpositionblock) {
        Vec3D hitVec = movingobjectpositionblock.getPos();
        return hitVec == null ? null : new Location(world, hitVec.getX(), hitVec.getY(), hitVec.getZ());
    }


}
