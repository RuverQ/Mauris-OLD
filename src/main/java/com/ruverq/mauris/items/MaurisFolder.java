package com.ruverq.mauris.items;

import com.ruverq.mauris.DataHelper;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MaurisFolder {

    public MaurisFolder(String name) {
        this.name = name;
    }

    @Getter
    String name;

    public boolean isValid(){
        File generalDir = DataHelper.getDir("mauris");
        File dir = new File(generalDir.getAbsolutePath() + File.separator + getName());

        return dir.exists();
    }

    public File getDir(){
        File generalDir = DataHelper.getDir("mauris");
        File dir = new File(generalDir.getAbsolutePath() + File.separator + getName());

        return dir;
    }

    public List<MaurisItem> getAllItems(boolean generate){
        File dir = getDir();

        File[] files = dir.listFiles();
        if(files == null) return null;

        List<MaurisItem> items = new ArrayList<>();

        for(File file : files){
            List<MaurisItem> itemsToLoad = MaurisItem.loadFromFile(file, this);
            for(MaurisItem mi : itemsToLoad){
                if(generate) mi.generate();
                items.add(mi);
            }
        }

        return items;
    }

    public File getFile(String path){
        path = path.replace("/", File.separator);
        File file = new File(getDir() + File.separator + path);
        return file;
    }

    public List<MaurisItem> getAllItems(){
        return getAllItems(false);
    }

    public static List<MaurisFolder> getAllFolders(){
        File dir = DataHelper.getDir("mauris");
        List<MaurisFolder> folders = new ArrayList<>();

        for(File folder : dir.listFiles()){
            if(folder.isDirectory()) folders.add(new MaurisFolder(folder.getName()));
        }

        return folders;
    }

}
