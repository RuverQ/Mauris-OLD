package com.ruverq.mauris.crafts.gui;

import com.ruverq.mauris.commands.CommandManager;
import com.ruverq.mauris.crafts.CraftingManager;
import com.ruverq.mauris.crafts.MaurisRecipe;
import com.ruverq.mauris.guibase.GUI;
import com.ruverq.mauris.guibase.slotGUI;
import com.ruverq.mauris.items.ItemsLoader;
import com.ruverq.mauris.items.MaurisItem;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ruverq.mauris.utils.FormatUtils.formatColor;

public class GUICraftingBench extends GUI {

    boolean shapeless;
    String discoverBy;

    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public void setDefaultSettings(){
        setDisplayName("Crafting Table: " + name);
        setSize(45);

        slotGUI lockSlot = new slotGUI(this);
        lockSlot.setItem(fastItem(Material.BLACK_STAINED_GLASS_PANE, "#000000."));
        lockSlot.setLock(true);
        for(int i = 0; i < this.size; i++){
            slotGUI slot = lockSlot.copy();
            slot.setSlotNumber(i);
            setItem(slot);
        }

        HashMap<Integer, slotGUI> avoidSlots = new HashMap<>();

        slotGUI craftBlockSlot = new slotGUI(this);
        craftBlockSlot.setLock(false);

        avoidSlots.put(10, craftBlockSlot);
        avoidSlots.put(11, craftBlockSlot);
        avoidSlots.put(12, craftBlockSlot);

        avoidSlots.put(19, craftBlockSlot);
        avoidSlots.put(20, craftBlockSlot);
        avoidSlots.put(21, craftBlockSlot);

        avoidSlots.put(28, craftBlockSlot);
        avoidSlots.put(29, craftBlockSlot);
        avoidSlots.put(30, craftBlockSlot);

        slotGUI result = new slotGUI(this);
        result.unlock();

        avoidSlots.put(24, result);

        slotGUI succeed = new slotGUI(this);
        succeed.setItem(fastItem(Material.GREEN_STAINED_GLASS_PANE, "#48e848Ready"));
        succeed.setLock(true);
        succeed.setSlotNumber(8);
        succeed.clickOn((t) -> {
            createCraft();
            this.close(player);
        });
        setItem(succeed);

        slotGUI type = new slotGUI(this);
        type.setSlotNumber(17);
        type.setLock(true);
        type.setItem(fastItem(Material.PURPLE_STAINED_GLASS_PANE, formatColor("#da75ffShaped")));
        type.clickOn((t) ->{
            shapeless = !shapeless;
            if(shapeless){
                type.setItem(fastItem(Material.PURPLE_STAINED_GLASS_PANE, formatColor("#ff5e7eShapeless")));
            }else{
                type.setItem(fastItem(Material.PURPLE_STAINED_GLASS_PANE, formatColor("#da75ffShaped")));
            }
            setItem(type);
        });
        setItem(type);

        slotGUI typeChange = new slotGUI(this);
        typeChange.setLock(true);
        typeChange.setSlotNumber(44);
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


        for(int slot : avoidSlots.keySet()){
            slotGUI slotGUI = avoidSlots.get(slot);
            slotGUI = slotGUI.copy();
            slotGUI.setSlotNumber(slot);
            setItem(slotGUI);
        }
    }

    @SneakyThrows
    private void createCraft(){

        ItemStack result = this.inventory.getItem(24);
        if(result == null) return;

        int resultAmount = result.getAmount();
        result.setAmount(1);

        String resultS = createItemIfNotExistsAndGetThat(result);
        if(resultS == null) return;

        File craft = new File(CraftingManager.getCrafts() + File.separator + name + ".yml");
        if(craft.exists()){
            player.sendMessage(CommandManager.getPrefix() + "Error while creating file");
            return;
        }

        craft.createNewFile();
        FileConfiguration yml_craft = YamlConfiguration.loadConfiguration(craft);
        yml_craft.set(name + ".enabled", true);
        yml_craft.set(name + ".result", resultS);
        yml_craft.set(name + ".resultAmount", resultAmount);
        yml_craft.set(name + ".discoverBy", "playerjoin");

        if(shapeless){

            List<String> ingredients = new ArrayList<>();
            for(int three = 0; three < 3; three++){
                for(int secondThree = 0; secondThree < 3 ; secondThree++){
                    ItemStack itemIngr = this.inventory.getItem(10 + secondThree + 9 * three);
                    if(itemIngr == null) continue;

                    MaurisItem mItem = ItemsLoader.getMaurisItem(itemIngr);
                    if(mItem == null){
                        String ingrS = createItemIfNotExistsAndGetThat(itemIngr);
                        if(ingrS == null) continue;
                        ingredients.add(ingrS);
                    }else{
                        ingredients.add(mItem.getName());
                    }
                }
            }

            yml_craft.set(name + ".type", "shapeless");
            if(ingredients.isEmpty()){
                player.sendMessage(CommandManager.getPrefix() + "genius");
                craft.delete();
                return;
            }

            yml_craft.set(name + ".craft.ingredients", ingredients);
        }else{
            //Collecting SHAPE
            StringBuilder sb = new StringBuilder();
            HashMap<Character, ItemStack> ingredients = new HashMap<>();
            HashMap<ItemStack, Character> ingredientsByItem = new HashMap<>();

            int chara = 0;
            for(int three = 0; three < 3; three++){
                if(three > 0){
                    sb.append("\n");
                }

                for(int secondThree = 0; secondThree < 3 ; secondThree++){
                    ItemStack itemIngr = this.inventory.getItem(10 + secondThree + 9 * three);
                    if(itemIngr != null && !itemIngr.getType().isAir()){
                        if(ingredientsByItem.containsKey(itemIngr)){
                            ingredients.put(ingredientsByItem.get(itemIngr), itemIngr);
                            ingredientsByItem.put(itemIngr, ingredientsByItem.get(itemIngr));
                            sb.append(ingredientsByItem.get(itemIngr));
                        }else{
                            ingredients.put(characters.toCharArray()[chara], itemIngr);
                            ingredientsByItem.put(itemIngr, characters.toCharArray()[chara]);
                            sb.append(characters.toCharArray()[chara]);
                            chara++;
                        }
                    }else{
                        sb.append(" ");
                    }
                }
            }

            if(ingredients.isEmpty()){
                player.sendMessage(CommandManager.getPrefix() + "genius");
                craft.delete();
                return;
            }

            yml_craft.set(name + ".type", "shaped");

            String[] shapeThing = sb.toString().split("\n");

            yml_craft.set(name + ".craft.shape.1", shapeThing[0]);
            yml_craft.set(name + ".craft.shape.2", shapeThing[1]);
            yml_craft.set(name + ".craft.shape.3", shapeThing[2]);

            ingredients.forEach((charar, itemm) ->{
                if(itemm != null){
                    String itemName = createItemIfNotExistsAndGetThat(itemm);

                    yml_craft.set(name + ".craft.ingredients." + charar, itemName);
                }
            });
        }

        yml_craft.save(craft);

        MaurisRecipe recipe = CraftingManager.getFRPRecipeFromYAML(name, yml_craft.getConfigurationSection(name));
        if(recipe == null){
            player.sendMessage(CommandManager.getPrefix() + "An error occurred while loading crafting. But the craft is saved");
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

    private ItemStack fastItem(Material material, String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(formatColor(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
