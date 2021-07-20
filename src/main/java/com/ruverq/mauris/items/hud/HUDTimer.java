package com.ruverq.mauris.items.hud;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.ResourcePackHelper;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.icons.MaurisIcon;
import com.ruverq.mauris.items.icons.MaurisOffsetIcon;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class HUDTimer extends BukkitRunnable {

    int i = 0;

    @Override
    public void run() {

        for(Player p : Bukkit.getOnlinePlayers()){
            if (!ResourcePackHelper.withResourcePack(p)) return;

            HUDStringBuilder buildRawHUD = new HUDStringBuilder();

            PlayerHUDInfo hudInfo = PlayerHUDInfoManager.getInfo(p);

            MaurisHUD hudBefore = null;

            System.out.println("-------------------");
            for(MaurisHUD hud : hudInfo.getHuds()){
                if(!hud.enabled) continue;

                MaurisIcon icon = hud.getIcon(hudInfo.getFrame(hud));
                if(icon == null) continue;

                System.out.println(buildRawHUD.currentOffset);
                buildRawHUD.getTo(hud.xOffset);
                System.out.println(buildRawHUD.currentOffset);

                hudInfo.setFrame(hud, 0);
                buildRawHUD.addIcon(icon);
                System.out.println(buildRawHUD.currentOffset);

                buildRawHUD.getTo(0);

                hudBefore = hud;
            }


            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(buildRawHUD.toString()));

            i++;
        }

    }

    private int getMinOffset(List<MaurisHUD> huds){

        int minOffset = 0;
        for(MaurisHUD hud : huds){
            minOffset = Math.min(minOffset, hud.xOffset);
        }

        return minOffset;
    }
}
