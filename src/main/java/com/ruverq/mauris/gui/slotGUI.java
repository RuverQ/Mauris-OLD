package com.ruverq.mauris.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

//ahaha lol lower case letter lol wtf lol ol oefeasd
public class slotGUI {

    boolean lock;
    ItemStack Item;
    int slotNumber;

    GUI gui;
    public slotGUI(GUI gui){
        this.gui = gui;
    }

    public void unlock(){
        lock = false;
    }

    public void lock(){
        lock = true;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public ItemStack getItem() {
        return Item;
    }

    public void setItem(ItemStack item) {
        Item = item;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    ClickOnSpecialSlotEvent clickOnSpecialSlotEvent;
    public void clickOn(ClickOnSpecialSlotEvent e){
        this.clickOnSpecialSlotEvent = e;
    }

    public void run(ClickType t){
        if(clickOnSpecialSlotEvent == null) return;
        clickOnSpecialSlotEvent.run(t);
    }

    public slotGUI copy(){
        slotGUI slotGUI = new slotGUI(gui);
        slotGUI.setItem(Item);
        slotGUI.setSlotNumber(slotNumber);
        slotGUI.setLock(lock);
        slotGUI.clickOn(clickOnSpecialSlotEvent);

        return slotGUI;
    }
}
