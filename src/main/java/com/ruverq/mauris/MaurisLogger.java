package com.ruverq.mauris;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class MaurisLogger{

    public MaurisLogger(String key) {
        this.key = key;
    }

    public static String prefix = "[Mauris] ";

    public Logger log = Bukkit.getLogger();

    public String key;

    public void info(String message){
        log.info(bm(message));
    }

    public void error(String message){
        log.warning(bm(message));
    }

    public void warning(String message){
        log.warning(bm(message));
    }

    private String bm(String message){
        return prefix + message;
    }

}
