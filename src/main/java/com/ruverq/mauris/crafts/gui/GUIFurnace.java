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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ruverq.mauris.utils.FormatUtils.formatColor;

public class GUIFurnace extends GUI {

    @Getter
    @Setter
    float experience;

    @Getter
    @Setter
    int cookingTime = 20;

    @Getter
    @Setter
    String type;

    @Getter
    @Setter
    String name;

    private static List<String> types(){
        List<String> a = new ArrayList<>();
        a.add("furnace");
        a.add("smoker");
        a.add("blast_furnace");
        a.add("campfire");
        return a;
    }

    public void setDefaultSettings(){
        setDisplayName("Craft in furnace: " + name);
        setSize(45);
        setType("furnace");

        slotGUI lockSlot = new slotGUI(this);
        lockSlot.setItem(fastItem(Material.BLACK_STAINED_GLASS_PANE, "#000000."));
        lockSlot.setLock(true);
        for(int i = 0; i < this.size; i++){
            slotGUI slot = lockSlot.copy();
            slot.setSlotNumber(i);
            setItem(slot);
        }

        slotGUI craftBlockSlot = new slotGUI(this);
        craftBlockSlot.setLock(false);
        craftBlockSlot.setSlotNumber(11);
        setItem(craftBlockSlot);

        slotGUI coal = new slotGUI(this);
        coal.setLock(true);
        coal.setItem(fastItem(Material.COAL, "#636363Уголёк"));
        coal.setSlotNumber(11 + 9 * 2);
        setItem(coal);

        slotGUI fire = new slotGUI(this);
        fire.setLock(true);
        fire.setItem(fastItem(Material.BLAZE_POWDER, "#ffb347Горение"));
        fire.setSlotNumber(11 + 9);
        setItem(fire);

        slotGUI result = new slotGUI(this);
        result.unlock();
        result.setSlotNumber(24);
        setItem(result);

        slotGUI succeed = new slotGUI(this);
        succeed.setItem(fastItem(Material.GREEN_STAINED_GLASS_PANE, "#48e848Готово"));
        succeed.setLock(true);
        succeed.setSlotNumber(8);
        succeed.clickOn((t) -> {
            createCraft();
            this.close(player);
        });
        setItem(succeed);

        // Experience ITEM
        slotGUI experience = new slotGUI(this);
        experience.setLock(true);

        ItemStack experienceItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
        List<String> experienceLore = new ArrayList<>();
        experienceLore.add(formatColor("#666666To add experience:"));
        experienceLore.add(formatColor("#99d1a3| LMB: +1"));
        experienceLore.add(formatColor("#d19e99| RMB: -1"));
        experienceLore.add(formatColor("#99d1a3| Shift + LMB: +10"));
        experienceLore.add(formatColor("#d19e99| Shift + RMB: -10"));
        experienceLore.add(formatColor("#dbd174| Mouse Wheel: Reset"));

        ItemMeta itemMetaexp = experienceItem.getItemMeta();
        itemMetaexp.setDisplayName(formatColor("#54ff71Smelting experience: " + getExperience()));
        itemMetaexp.setLore(experienceLore);

        experienceItem.setItemMeta(itemMetaexp);
        experience.setSlotNumber(8 + 9);

        experience.setItem(experienceItem);

        experience.clickOn((t) ->{
            if(t == ClickType.LEFT){
                setExperience(getExperience() + 1);
            }else if(t == ClickType.RIGHT){
                setExperience(getExperience() - 1);
            }else if(t == ClickType.SHIFT_LEFT){
                setExperience(getExperience() + 10);
            }else if(t == ClickType.SHIFT_RIGHT){
                setExperience(getExperience() - 10);
            }else if(t == ClickType.MIDDLE){
                setExperience(0);
            }
            if(getExperience() < 0){
                setExperience(0);
            }

            itemMetaexp.setDisplayName(formatColor("#54ff71Smelting experience: " + getExperience()));
            experienceItem.setItemMeta(itemMetaexp);

            setItem(experience);
        });
        setItem(experience);

        //Time to cook ITEM
        slotGUI clock = new slotGUI(this);
        clock.setLock(true);

        ItemStack clockitemItem = new ItemStack(Material.CLOCK);
        List<String> clockLore = new ArrayList<>();
        clockLore.add(formatColor("#666666To add time:"));
        clockLore.add(formatColor("#99d1a3| LMB: +1"));
        clockLore.add(formatColor("#d19e99| RMB: -1"));
        clockLore.add(formatColor("#99d1a3| Shift + LMB: +20"));
        clockLore.add(formatColor("#d19e99| Shift + RMB: -20"));
        clockLore.add(formatColor("#dbd174| Mouse Wheel: Reset"));

        ItemMeta itemMetaclock = clockitemItem.getItemMeta();
        itemMetaclock.setDisplayName(formatColor("#54ff71Melting time: " + getCookingTime() + "ticks #666666(" + getCookingTime() / 20 + "sec)"));
        itemMetaclock.setLore(clockLore);

        clockitemItem.setItemMeta(itemMetaclock);
        clock.setSlotNumber(8 + 9 + 9);

        clock.setItem(clockitemItem);

        clock.clickOn((t) ->{
            if(t == ClickType.LEFT){
                setCookingTime(getCookingTime() + 1);
            }else if(t == ClickType.RIGHT){
                setCookingTime(getCookingTime() - 1);
            }else if(t == ClickType.SHIFT_LEFT){
                setCookingTime(getCookingTime() + 20);
            }else if(t == ClickType.SHIFT_RIGHT){
                setCookingTime(getCookingTime() - 20);
            }else if(t == ClickType.MIDDLE){
                setCookingTime(0);
            }
            if(getCookingTime() < 20){
                setCookingTime(20);
            }

            itemMetaclock.setDisplayName(formatColor("#54ff71Melting time: " + getCookingTime() + "ticks #666666(" + getCookingTime() / 20 + "sec)"));
            clockitemItem.setItemMeta(itemMetaclock);

            setItem(clock);
        });
        setItem(clock);

        slotGUI changeFType = new slotGUI(this);
        changeFType.setLock(true);
        changeFType.setSlotNumber(22);
        changeFType.setItem(fastItem(Material.FURNACE, "#f0b359In furnace",formatColor("#474747Click to change to craft in the smoker")));
        changeFType.clickOn((t) -> {
            if(t == ClickType.DOUBLE_CLICK){
                return;
            }

            int i = 0;
            for(String typeS : types()){
                if(typeS.equalsIgnoreCase(type)){
                    if(types().size() == i + 1){
                        setType(types().get(0));
                    }else{
                        setType(types().get(i + 1));
                    }
                    continue;
                }
                i++;
            }

            if(type.equalsIgnoreCase("furnace")){
                changeFType.setItem(fastItem(Material.FURNACE, "#f0b359In furnace", formatColor("#474747Click to change to craft in the smoker")));
                setItem(changeFType);
            }else if(type.equalsIgnoreCase("smoker")){
                changeFType.setItem(fastItem(Material.SMOKER, "#baa88cIn smoker", formatColor("#474747Click to change to craft in the blast furnace")));
                setItem(changeFType);
            }else if(type.equalsIgnoreCase("blast_furnace")){
                changeFType.setItem(fastItem(Material.BLAST_FURNACE, "#f25e30In blast furnace", formatColor("#474747Click to change to crafting on the campfire")));
                setItem(changeFType);
            }else if(type.equalsIgnoreCase("campfire")){
                changeFType.setItem(fastItem(Material.CAMPFIRE, "#ffb036On campfire", formatColor("#474747Click to change to crafting in the furnace")));
                setItem(changeFType);
            }
        });

        setItem(changeFType);

        slotGUI craftBenchBackItem = new slotGUI(this);
        craftBenchBackItem.setItem(fastItem(Material.CRAFTING_TABLE, "#ffc954Switch to crafting in the crafting table"));
        craftBenchBackItem.setLock(true);
        craftBenchBackItem.setSlotNumber(9 * 4);
        craftBenchBackItem.clickOn((t) ->{
            GUICraftingBench craftingBench = new GUICraftingBench();
            craftingBench.setName(name);
            craftingBench.setDefaultSettings();
            craftingBench.setFor(player);

            this.close(player);
            craftingBench.createAndOpen();
        });

        setItem(craftBenchBackItem);

        slotGUI smithingItem = new slotGUI(this);
        smithingItem.setItem(fastItem(Material.SMITHING_TABLE, "#c8e6e5Switch to crafting in smithing"));
        smithingItem.setLock(true);
        smithingItem.setSlotNumber(9 * 5 - 1);
        smithingItem.clickOn((t) ->{
            GUISmithingRecipe guiSmithingRecipe = new GUISmithingRecipe();
            guiSmithingRecipe.setName(name);
            guiSmithingRecipe.setDefaultSettings();
            guiSmithingRecipe.setFor(player);

            this.close(player);

            guiSmithingRecipe.createAndOpen();
        });
        setItem(smithingItem);
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


        String resultS = createItemIfNotExistsAndGetThat(result);

        ItemStack ingredient = this.inventory.getItem(11);
        if(ingredient == null || ingredient.getType().isAir()){
            player.sendMessage(CommandManager.getPrefix() + "genius");
            return;
        }
        String ingredientS = createItemIfNotExistsAndGetThat(ingredient);

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
        yml_craft.set(name + ".type", type);
        yml_craft.set(name + ".discoverBy", "playerjoin");

        yml_craft.set(name + ".craft.ingredient", ingredientS);
        yml_craft.set(name + ".craft.experience", getExperience());
        yml_craft.set(name + ".craft.cookingTime", getCookingTime());

        yml_craft.save(craft);

        MaurisRecipe recipe = CraftingManager.getFRPRecipeFromYAML(name, yml_craft.getConfigurationSection(name));
        if(recipe == null){
            player.sendMessage(CommandManager.getPrefix() + "An error occured while loading crafting. But the craft is saved");
            return;
        }

        CraftingManager.loadCraft(recipe);

        player.sendMessage(CommandManager.getPrefix() + "The craft has been successfully saved and already loaded");
    }

    public String createItemIfNotExistsAndGetThat(ItemStack result){
        String resultS = result.getType().name();
        if(!result.isSimilar(new ItemStack(result.getType()))){
            MaurisItem item = ItemsLoader.getMaurisItem(result);
            if(item == null){
                return result.getType().name().toLowerCase();
            }
            resultS = item.getName();
        }

        return resultS;
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

    private ItemStack fastItem(Material material, String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(formatColor(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
