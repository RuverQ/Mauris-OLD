package com.ruverq.mauris.crafts.events;

import com.ruverq.mauris.crafts.MaurisRecipe;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public class MaurisCraftEvent extends CraftItemEvent {

    @Getter
    MaurisRecipe maurisRecipe;

    public MaurisCraftEvent(@NotNull Recipe recipe, @NotNull InventoryView what, @NotNull InventoryType.SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action, MaurisRecipe maurisRecipe) {
        super(recipe, what, type, slot, click, action);
        this.maurisRecipe = maurisRecipe;
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
