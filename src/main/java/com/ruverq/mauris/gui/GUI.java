package com.ruverq.mauris.gui;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI implements Listener {

    Inventory inventory;

    @Getter
    Player player;

    String displayName;

    public List<slotGUI> slots = new ArrayList<>();
    private HashMap<Integer, slotGUI> slotsWithIndes = new HashMap<>();
    public static HashMap<Inventory, GUI> guiS = new HashMap<>();

    public void create(){
        if(inventory != null){
            guiS.remove(inventory);
        }

        inventory = Bukkit.createInventory(player, size, displayName);
        for(slotGUI slotGUI: slots){
            inventory.setItem(slotGUI.getSlotNumber(), slotGUI.getItem());
        }
        guiS.put(inventory, this);
    }

    public void createAndOpen(){
        if(inventory != null){
            guiS.remove(inventory);
        }
        inventory = Bukkit.createInventory(player, size, displayName);

        for(slotGUI slotGUI: slots){
            inventory.setItem(slotGUI.getSlotNumber(), slotGUI.getItem());
        }
        open();
        guiS.put(inventory, this);
    }

    public void setDisplayName(String displayName) {
        this.displayName = format(displayName);
    }

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static String format(String msg){
        Matcher matcher = pattern.matcher(msg);
        while(matcher.find()){
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, String.valueOf(ChatColor.of(color)));
            matcher = pattern.matcher(msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static GUI getGUI(Inventory inventory){
        return guiS.get(inventory);
    }

    public slotGUI getSlot(int i){
        return slotsWithIndes.get(i);
    }

    int size;
    public void setSize(int size){
        this.size = size;
    }

    public void setItem(slotGUI slot){
        if(slotsWithIndes.get(slot.getSlotNumber()) != null){
            slotGUI slotGUI = getSlot(slot.getSlotNumber());
            slotsWithIndes.remove(slot.getSlotNumber());
            slots.remove(slotGUI);
        }
        slots.add(slot);
        slotsWithIndes.put(slot.getSlotNumber(), slot);

        if(inventory != null){
            inventory.setItem(slot.getSlotNumber(), slot.getItem());
        }
    }

    public void addItem(slotGUI slot){
        slotsWithIndes.put(slots.size(), slot);
        slots.add(slot);

        if(inventory != null){
            inventory.setItem(slots.size(), slot.getItem());
        }
    }

    public static boolean exists(Inventory inventory){
        return guiS.get(inventory) != null;
    }

    public void openFor(Player p){
        if(inventory == null){
            create();
        }

        p.openInventory(inventory);
    }

    public void open(){
        if(inventory == null){
            create();
        }

       player.openInventory(inventory);
    }

    public void close(Player p){
        p.closeInventory();
    }

    public void setFor(Player p){
        this.player = p;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        Inventory clickInv = e.getClickedInventory();

        GUI gui = GUI.getGUI(clickInv);
        if(gui == null) return;

        int slotClicked = e.getSlot();
        slotGUI slot = gui.getSlot(slotClicked);
        if(slot == null) {
            e.setCancelled(true);
            return;
        }else if(slot.isLock()){
            e.setCancelled(true);
        }
        slot.run(e.getClick());
    }
    @EventHandler
    public void onInvDrag(InventoryDragEvent e){
        Inventory clickInv = e.getInventory();

        GUI gui = GUI.getGUI(clickInv);
        if(gui == null) return;

        for(int a : e.getInventorySlots()){
            slotGUI slot = gui.getSlot(a);
            if(slot == null) {
                e.setCancelled(true);
                return;
            }else if(slot.isLock()){
                slot.run(ClickType.UNKNOWN);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e){

    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e){

    }
}
