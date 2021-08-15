package com.ruverq.mauris.items.gui;

import com.ruverq.mauris.guibase.GUI;
import com.ruverq.mauris.guibase.slotGUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import com.ruverq.mauris.utils.ItemBuilder;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemsGUI extends ItemsPageGUI {

    public static final int itemsOnPage = 45;

    List<MaurisItem> maurisItems = ItemsLoader.getMaurisItems();

    @Override
    public void setMaurisItems(List<MaurisItem> maurisItems) {
        super.setMaurisItems(maurisItems);
        this.maurisItems = maurisItems;
    }

    int currentPage = 0;

    public void setDefaultSettings(){
        currentPage = 0;

        setPage(currentPage);
    }

    private void setPage(int page){
        if(page < 0){
            page = 0;
            currentPage = page;
        }

        setSize(54);

        slotGUI leftPage = new slotGUI(this);
        leftPage.setItem(new ItemBuilder().fastItem("&rLeft Page", Material.FEATHER).build());
        leftPage.setSlotNumber(45);
        leftPage.setLock(true);
        leftPage.clickOn((ct) ->{
            clear();
            currentPage--;
            setPage(currentPage);
            openFor(getPlayer());
        });

        slotGUI rightPage = new slotGUI(this);
        rightPage.setItem(new ItemBuilder().fastItem("&rRight Page", Material.ARROW).build());
        rightPage.setSlotNumber(53);
        rightPage.setLock(true);
        rightPage.clickOn((ct)->{
            clear();
            currentPage++;
            setPage(currentPage);
            openFor(getPlayer());
        });

        slotGUI backToMenu = new slotGUI(this);
        backToMenu.setItem(new ItemBuilder().fastItem("&rBack to Menu", Material.KNOWLEDGE_BOOK).build());
        backToMenu.setSlotNumber(49);
        backToMenu.setLock(true);
        backToMenu.clickOn((ct) ->{
            FoldersGUI fg = new FoldersGUI();
            fg.setDefaultSettings();
            fg.openFor(getPlayer());
        });

        boolean rightContains = !(maurisItems.size() < ((page + 1) * itemsOnPage));
        boolean leftContains = currentPage > 0;

        if(leftContains){
            setItem(leftPage);
        }

        if(rightContains){
            setItem(rightPage);
        }

        setItem(backToMenu);

        setUpItems(page * itemsOnPage);
    }

}
