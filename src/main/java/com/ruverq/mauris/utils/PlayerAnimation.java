package com.ruverq.mauris.utils;

import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerAnimation {

    public static void play(Player player){
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 0);

        Location block = player.getLocation();

        ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby(
                null,
                block.getX(),
                block.getY(),
                block.getZ(),
                120,
                ((CraftWorld) block.getWorld()).getHandle().getDimensionKey(),
                packet);
    }

}
