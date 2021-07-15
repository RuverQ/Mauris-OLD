package com.ruverq.mauris.items.hud;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.ResourcePackHelper;
import com.ruverq.mauris.items.ItemsLoader;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HUDTimer extends BukkitRunnable {

    int i;

    @Override
    public void run() {

        for(Player p : Bukkit.getOnlinePlayers()){
            if (!ResourcePackHelper.withResourcePack(p)) return;

            StringBuilder buildRawHUD = new StringBuilder();

            PlayerHUDInfo hudInfo = PlayerHUDInfoManager.getInfo(p);

            for(MaurisHUD hud : hudInfo.hudsFrame.keySet()){
                for(int i = 0; i < hud.xOffset; i++){
                    buildRawHUD.append("\u0000");
                }
                hudInfo.setFrame(hud, i % 2);
                buildRawHUD.append(hud.getIcon(hudInfo.getFrame(hud)).getNormalizedSymbol());
            }

            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(buildRawHUD.toString()));

            i++;
        }

    }

}
