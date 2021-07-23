package com.ruverq.mauris.items.hud;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.items.ItemsLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerHUDInfoManager implements Listener {

    static HUDCheker currentTimer;

    public static void setUp(){
        infos.clear();
        for(Player p : Bukkit.getOnlinePlayers()){
            addDefaultPlayerInfo(p);
        }
        if(currentTimer != null) currentTimer.cancel();
        currentTimer = new HUDCheker();
        currentTimer.runTaskTimer(Mauris.getInstance(), 0, 5);
    }

    static HashMap<Player, PlayerHUDInfo> infos = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        addDefaultPlayerInfo(p);
    }

    private static void addDefaultPlayerInfo(Player p){
        PlayerHUDInfo playerHUDInfo = new PlayerHUDInfo(p);
        for(MaurisHUD hud : ItemsLoader.getHuds().values()){
            playerHUDInfo.hudsFrame.put(hud, 0);
        }

        infos.put(p, playerHUDInfo);
    }

    public static PlayerHUDInfo getInfo(Player p){
        return infos.get(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        infos.remove(p);
    }


}
