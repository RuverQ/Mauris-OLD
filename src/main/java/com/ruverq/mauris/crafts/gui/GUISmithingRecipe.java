package com.ruverq.mauris.crafts.gui;

import com.ruverq.mauris.commands.CommandManager;
import com.ruverq.mauris.crafts.CraftingManager;
import com.ruverq.mauris.crafts.MaurisRecipe;
import com.ruverq.mauris.guibase.GUI;
import com.ruverq.mauris.guibase.slotGUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ruverq.mauris.utils.FormatUtils.formatColor;

public class GUISmithingRecipe extends GUI {

    @Getter
    @Setter
    String name;

    public void setDefaultSettings(){
        setDisplayName("Crafting in smithing: " + name);
        setSize(45);

        slotGUI lockSlot = new slotGUI(this);
        lockSlot.setItem(fastItem(Material.BLACK_STAINED_GLASS_PANE, "#000000."));
        lockSlot.setLock(true);
        for(int i = 0; i < this.size; i++){
            slotGUI slot = lockSlot.copy();
            slot.setSlotNumber(i);
            setItem(slot);
        }

        slotGUI firstIngredient = new slotGUI(this);
        firstIngredient.setLock(false);
        firstIngredient.setSlotNumber(19);
        setItem(firstIngredient);

        slotGUI secondIngredient = new slotGUI(this);
        secondIngredient.setLock(false);
        secondIngredient.setSlotNumber(21);
        setItem(secondIngredient);

        slotGUI result = new slotGUI(this);
        result.setLock(false);
        result.setSlotNumber(24);
        setItem(result);

        slotGUI succeed = new slotGUI(this);
        succeed.setItem(fastItem(Material.GREEN_STAINED_GLASS_PANE, "#48e848Ready"));
        succeed.setLock(true);
        succeed.setSlotNumber(8);
        succeed.clickOn((t) -> {
            createCraft();
            this.close(player);
        });
        setItem(succeed);

        slotGUI typeChange = new slotGUI(this);
        typeChange.setLock(true);
        typeChange.setSlotNumber(9 * 4);
        typeChange.setItem(fastItem(Material.FURNACE, formatColor("#ffc954Switch to crafting in the furnace")));
        typeChange.clickOn((t) ->{
            GUIFurnace furnace = new GUIFurnace();
            furnace.setSize(45);
            furnace.setName(name);
            furnace.setDefaultSettings();
            furnace.setFor(player);
            this.close(player);

            furnace.createAndOpen();
        });
        setItem(typeChange);


    }

    @SneakyThrows
    private void createCraft() {
        ItemStack result = this.inventory.getItem(24);
        if(result == null){
            player.sendMessage(CommandManager.getPrefix() + "genius");
            return;
        }

        int resultAmount = result.getAmount();
        result.setAmount(1);

        String resultS = result.getType().name();
        if(!result.isSimilar(new ItemStack(result.getType()))){
            MaurisItem item = ItemsLoader.getMaurisItem(result);
            if(item != null){
                resultS = item.getName();
            }
        }

        ItemStack fingredient = this.inventory.getItem(19);
        if(fingredient == null || fingredient.getType().isAir()){
            player.sendMessage(CommandManager.getPrefix() + "genius");
            return;
        }

        ItemStack singredient = this.inventory.getItem(21);
        if(singredient == null || singredient.getType().isAir()){
            player.sendMessage(CommandManager.getPrefix() + "genius");
            return;
        }

        File craft = new File(CraftingManager.getCrafts() + File.separator + name + ".yml");
        if(craft.exists()){
            player.sendMessage(CommandManager.getPrefix() + "An error occurred while saving the file");
            return;
        }

        craft.createNewFile();
        FileConfiguration yml_craft = YamlConfiguration.loadConfiguration(craft);
        yml_craft.set(name + ".enabled", true);
        yml_craft.set(name + ".result", resultS);
        yml_craft.set(name + ".resultAmount", resultAmount);
        yml_craft.set(name + ".discoverBy", "playerjoin");
        yml_craft.set(name + ".type", "smithing");

        yml_craft.set(name + ".craft.firstIngredient", fingredient.getType().name());
        yml_craft.set(name + ".craft.secondIngredient", singredient.getType().name());

        yml_craft.save(craft);

        MaurisRecipe recipe = CraftingManager.getFRPRecipeFromYAML(name, yml_craft.getConfigurationSection(name));
        if(recipe == null){
            player.sendMessage(CommandManager.getPrefix() + "An error occurred while loading crafting. But the craft is saved");
            return;
        }

        CraftingManager.loadCraft(recipe);
        player.sendMessage(CommandManager.getPrefix() + "The craft has been successfully saved and already loaded");
    }

    private ItemStack fastItem(Material material, String name, String... lore){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(formatColor(name));

        List<String> loree = new ArrayList<>(Arrays.asList(lore));
        itemMeta.setLore(loree);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
