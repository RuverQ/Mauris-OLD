package com.ruverq.mauris.items;

import com.ruverq.mauris.DataHelper;
import com.ruverq.mauris.items.blocks.MaurisBlock;
import com.ruverq.mauris.items.icons.MaurisIcon;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ItemsLoader {

    //Please help
    @Getter
    static List<MaurisItem> itemsLoaded = new ArrayList<>();

    static HashMap<String, MaurisItem> itemsByFullName = new HashMap<>();

    @Getter
    static HashMap<String, MaurisItem> itemsByName = new HashMap<>();
    static HashMap<ItemStack, MaurisItem> itemsByItemStack = new HashMap<>();

    @Getter
    static HashMap<BlockData, MaurisBlock> blockByBlockData = new HashMap<>();
    static HashMap<String, List<MaurisItem>> itemsByFolder = new HashMap<>();

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

        List<MaurisItem> items = new ArrayList<>();

        deleteGeneratedFolders();

        for(MaurisFolder folder : MaurisFolder.getAllFolders()){
            List<MaurisItem> folderItems = folder.getAllItems(false);
            items.addAll(folderItems);
        }

        //pls kkill me
        loadForStructure(items);
        deleteUnnecessaryIDs();

        MaurisItem.generate(items);
        loadForStructure(items);
    }

    //Cringe
    private static void deleteUnnecessaryIDs(){
        ConfigurationSection cs = DataHelper.getFileAsYAML("ids.yml");
        for(String sec : cs.getConfigurationSection("").getKeys(false)){
            for(String dopSec : cs.getConfigurationSection(sec).getKeys(false)){
                for(String name : cs.getConfigurationSection(sec + "." + dopSec).getKeys(false)){
                    MaurisItem mi = getMaurisItem(name);
                    if(mi != null) continue;
                    DataHelper.removeId(name, sec + "." + dopSec);
                }
            }
        }
    }

    //TODO Create a service that will not fuck files and that will delete all needed files at the start
    private static void deleteGeneratedFolders(){

        DataHelper.deleteFile("resource_pack/assets/minecraft/font/default.json");
        DataHelper.deleteFile("resource_pack/assets/minecraft/blockstates/note_block.json");
        DataHelper.deleteFile("resource_pack/assets/minecraft/blockstates/mushroom_stem.json");
        DataHelper.deleteFile("resource_pack/assets/minecraft/blockstates/tripwire.json");

        for(MaurisFolder mf : MaurisFolder.getAllFolders()){
            DataHelper.deleteFile("resource_pack/assets/minecraft/models/" + mf.getName() + "/generated");
        }

    }

    public static Set<String> getLoadedFolders(){
        return itemsByFolder.keySet();
    }

    public static List<MaurisItem> getMaurisItems(String folder){
        return itemsByFolder.get(folder);
    }

    public static MaurisBlock getMaurisBlock(BlockData blockData){
        return blockByBlockData.get(blockData);
    }

    public static List<MaurisItem> getMaurisItems(){
        return itemsLoaded;
    }

    public static MaurisItem getMaurisItem(int index){
        return itemsLoaded.get(index);
    }

    public static MaurisItem getMaurisItem(ItemStack itemStack){
        return itemsByItemStack.get(itemStack);
    }

    public static MaurisItem getMaurisItem(String name){
        MaurisItem item = itemsByFullName.get(name);
        if(item != null) return item;

        return itemsByName.get(name);
    }

    public static ItemStack getMaurisItem(String name, boolean materialIfNull){
        MaurisItem maurisItem = getMaurisItem(name);
        if(maurisItem == null){
            if(!materialIfNull) return null;
            Material material = Material.matchMaterial(name);
            if(material == null) return null;
            return new ItemStack(material);
        }

        return maurisItem.getAsItemStack();

    }

    private static void loadForStructure(List<MaurisItem> items){
        itemsLoaded.clear();
        itemsByName.clear();
        itemsByItemStack.clear();
        blockByBlockData.clear();
        itemsByFolder.clear();
        itemsByFullName.clear();

        for(MaurisItem item : items){
            loadForStructure(item);
        }
    }

    private static void loadForStructure(MaurisItem item){
        itemsLoaded.add(item);
        itemsByName.put(item.name, item);

        itemsByFullName.put(item.getFolder().getName() + ":" + item.getName(), item);

        if(item instanceof MaurisIcon) return;

        itemsByItemStack.put(item.getAsItemStack(), item);

        if(item instanceof MaurisBlock){
            MaurisBlock block = (MaurisBlock) item;
            blockByBlockData.put(block.getAsBlockData(), block);
        }

        List<MaurisItem> items = itemsByFolder.get(item.folder.getName());
        if(items == null){
            items = new ArrayList<>();
            items.add(item);
        }else{
            items.add(item);
            itemsByFolder.remove(item.folder.getName());
        }
        itemsByFolder.put(item.folder.getName(), items);

    }

}
