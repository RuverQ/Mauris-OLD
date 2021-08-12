package com.ruverq.mauris.items.gui;

import com.ruverq.mauris.guibase.GUI;
import com.ruverq.mauris.guibase.slotGUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemsPageGUI extends GUI {

    @Setter
    int startItem;

    @Setter
    List<MaurisItem> maurisItems = new ArrayList<>();

    public void setUpItems(int startItem){

        int slot = 0;

        for(int i = startItem; i < startItem + ItemsGUI.itemsOnPage; i++){

            if(i > maurisItems.size() - 1) break;
            MaurisItem item = maurisItems.get(i);

            slotGUI slotGUI = new slotGUI(this);
            slotGUI.setItem(item.getAsItemStack());
            slotGUI.setLock(true);
            slotGUI.setSlotNumber(slot);

            slotGUI.clickOn((e) ->{
                if(e.isShiftClick()){
                    ItemStack itemStack = item.getAsItemStack();
                    itemStack.setAmount(64);
                    getPlayer().getInventory().addItem(itemStack);
                }else{
                    getPlayer().setItemOnCursor(item.getAsItemStack());
                }
                getPlayer().updateInventory();
            });

            setItem(slotGUI);

            slot++;
        }

    }

}
