package com.ruverq.mauris.items.hud;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeChecker {

    @Getter
    GameMode gameMode;

    @Getter
    boolean value;

    @Getter
    boolean result;

    @Getter
    String gameModeS;

    public GameModeChecker(GameMode gameMode, boolean value){
        this.gameMode = gameMode;
        this.gameModeS = gameMode.name().toLowerCase();
        this.value = value;
    }

    public boolean check(Player player){
        if(player.getGameMode() != gameMode) return true;

        result = (player.getGameMode() == gameMode) == value;
        return result;
    }

}
