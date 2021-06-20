package com.ruverq.mauris.utils;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerAnimation {

    public static void play(Player player){
        PlayerConnection connection= ((CraftPlayer)player).getHandle().playerConnection;

        connection.sendPacket(new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 0));
    }

}
