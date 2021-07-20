package com.ruverq.mauris.items.hud;

import com.ruverq.mauris.items.ItemsLoader;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerHUDInfo {

    public PlayerHUDInfo(Player player) {
        this.player = player;
    }

    HashMap<MaurisHUD, Integer> hudsFrame = new HashMap<>();

    @Getter
    Player player;

    void setFrame(String huds, int frame){
        MaurisHUD hud = (MaurisHUD) ItemsLoader.getMaurisItem(huds);
        hudsFrame.remove(hud);
        hudsFrame.put(hud, frame);
    }

    public List<MaurisHUD> getHuds(){
        return new ArrayList<>(hudsFrame.keySet());
    }

    void setFrame(MaurisHUD hud, int frame){
        hudsFrame.remove(hud);
        hudsFrame.put(hud, frame);
    }

    int getFrame(MaurisHUD hud){
        return hudsFrame.get(hud);
    }

    int getFrame(String name){
        for(MaurisHUD hud : hudsFrame.keySet()){
            if(hud.getName().equalsIgnoreCase(name)){
                return hudsFrame.get(hud);
            }
        }
        return -1;
    }

}
