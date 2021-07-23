package com.ruverq.mauris.items.gui;

import com.ruverq.mauris.guibase.GUI;
import com.ruverq.mauris.guibase.slotGUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import lombok.Setter;

import java.util.List;

public class ItemsGUI extends GUI {

    @Setter
    List<MaurisItem> maurisItems = ItemsLoader.getMaurisItems();

    public void setDefaultSettings(){

        int slot = 0;
        for(MaurisItem item : maurisItems){

            slotGUI slotGUI = new slotGUI(this);
            slotGUI.setItem(item.getAsItemStack());
            slotGUI.setLock(true);
            slotGUI.setSlotNumber(slot);

            slotGUI.clickOn((e) ->{
                getPlayer().getInventory().addItem(item.getAsItemStack());
                getPlayer().updateInventory();
            });

            addItem(slotGUI);

            slot++;
        }
        setSize(54);

    }

}
