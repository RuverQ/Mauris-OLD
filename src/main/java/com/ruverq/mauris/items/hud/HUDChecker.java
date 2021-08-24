package com.ruverq.mauris.items.hud;

import com.ruverq.mauris.ResourcePackHelper;
import com.ruverq.mauris.items.icons.MaurisIcon;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class HUDChecker extends BukkitRunnable {

    @Override
    public void run() {
        check();
    }

    public static void check(){
        for(Player p : Bukkit.getOnlinePlayers()){
            check(p);
        }
    }

    public static void check(Player p){
        if (!ResourcePackHelper.withResourcePack(p)) return;

        HUDStringBuilder buildRawHUD = new HUDStringBuilder();

        PlayerHUDInfo hudInfo = PlayerHUDInfoManager.getInfo(p);

        for(MaurisHUD hud : hudInfo.getHuds()){
            if(hud == null) continue;
            if(!hud.enabled) continue;

            if(!checkVisibility(hud, p)) continue;

            int frame = hudInfo.getFrame(hud);
            if(hud.vanillaIterator){
                buildRawHUD.getTo(hud.xOffset);

                for(int i = 1; i <= hud.vanillaIterate; i++){
                    int actualFrame = frame;
                    if(actualFrame > hud.frames.size() - 1) actualFrame = hud.frames.size() - 1;
                    if(actualFrame < 0) actualFrame = 0;
                    MaurisIcon icon = hud.getIcon(actualFrame);
                    buildRawHUD.getTo((icon.getHeight() - 2) * i);
                    buildRawHUD.addIcon(icon);
                    frame -= hud.frames.size() - 1;
                }
                buildRawHUD.getTo(0);
                continue;
            }

            MaurisIcon icon = hud.getIcon(frame);
            if(icon == null) continue;

            buildRawHUD.getTo(hud.xOffset);

            buildRawHUD.addIcon(icon);

            buildRawHUD.getTo(0);
        }


        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(buildRawHUD.toString()));
    }

    private static boolean checkVisibility(MaurisHUD hud, Player player){

        boolean visible = false;

        for(GameModeChecker checker : hud.gameModeCheckers){
            visible = checker.check(player);
            if(!visible) return false;
        }

        if(!hud.underwaterVisibility && player.getRemainingAir() < player.getMaximumAir()) visible = false;

        return visible;
    }

    private int getMinOffset(List<MaurisHUD> huds){

        int minOffset = 0;
        for(MaurisHUD hud : huds){
            minOffset = Math.min(minOffset, hud.xOffset);
        }

        return minOffset;
    }
}
