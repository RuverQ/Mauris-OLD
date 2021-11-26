package com.ruverq.mauris.utils;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class VersionChecker {

    public static boolean isPaper(){
        boolean isPapermc = false;
        try {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData");
            isPapermc = true;
        } catch (ClassNotFoundException ignored) {

        }

        return isPapermc;
    }

}
