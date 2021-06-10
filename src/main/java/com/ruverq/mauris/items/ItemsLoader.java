package com.ruverq.mauris.items;

import com.ruverq.mauris.DataHelper;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemsLoader {

    static List<MaurisItem> itemsLoaded = new ArrayList<>();

    public static void load(){

        File dir = DataHelper.getDir("mauris");
        if(dir == null){
            Bukkit.getLogger().warning("ITEMS CANNOT LOAD WTF");
            return;
        }
        File[] dirListFiles = dir.listFiles();
        if(dirListFiles == null){
            Bukkit.getLogger().warning("ITEMS CANNOT LOAD WTF");
            return;
        }

        String currentFolder = "";
        for(File dirItems : dirListFiles){
            if(!dirItems.isDirectory()) continue;
            currentFolder = dirItems.getName();
            File[] files = dirItems.listFiles();
            if(files == null) return;

            for(File file : files){

                List<MaurisItem> itemsToLoad = MaurisItem.loadFromFile(file, currentFolder);
                for(MaurisItem mi : itemsToLoad){
                    mi.generate();
                    System.out.println("Loaded " + mi.name);
                    itemsLoaded.add(mi);
                }

            }

        }


    }

}
