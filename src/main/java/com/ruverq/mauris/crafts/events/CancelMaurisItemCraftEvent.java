package com.ruverq.mauris.crafts.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;

public class CancelMaurisItemCraftEvent extends PrepareItemCraftEvent implements Cancellable {

    public CancelMaurisItemCraftEvent(CraftingInventory what, InventoryView view, boolean isRepair) {
        super(what, view, isRepair);
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    boolean canceled = false;

    @Override
    public void setCancelled(boolean cancel) {
        cancel = canceled;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
