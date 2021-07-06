package com.ruverq.mauris.utils;

import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerAnimation {

    public static void play(Player player){
        PlayerConnection connection= ((CraftPlayer)player).getHandle().b;

        connection.sendPacket(new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 0));
    }

}
