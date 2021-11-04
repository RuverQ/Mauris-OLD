package com.ruverq.mauris.crafts;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.crafts.discover.DiscoverManager;
import com.ruverq.mauris.crafts.types.RecipePreloadInformation;
import com.ruverq.mauris.crafts.types.RecipeType;
import com.ruverq.mauris.crafts.types.RecipeTypeManager;
import com.ruverq.mauris.items.ItemsLoader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CraftingManager {

    @Getter
    public static List<MaurisRecipe> loadedRecipes = new ArrayList<>();

    @Getter
    private static File crafts;
    private static boolean default_onlyItemStacks;

    @SneakyThrows
    public static void setUp(boolean reload){
        loadedRecipes.clear();
        RecipeTypeManager.setUp();

        crafts = new File(Mauris.getInstance().getDataFolder() + File.separator + "crafts");;
        default_onlyItemStacks = Mauris.getInstance().getConfig().getBoolean("crafts.onlyItemStacks", true);

        DiscoverManager.unload();
        DiscoverManager.setUp(reload);
        disableCrafts();
        createFolders();
        loadCrafts();
    }

    public static List<String> getNamespaceKeysOfRecipes() {
        List<String> recipes = new ArrayList<>();
        for(MaurisRecipe mrecipe : loadedRecipes){
            Recipe recipe = mrecipe.getRecipe();
            if(recipe instanceof Keyed){
                Keyed recipeKeyed = (Keyed) recipe;
                String fullkey = recipeKeyed.getKey().getNamespace() + ":" + recipeKeyed.getKey().getKey();
                recipes.add(fullkey);
            }
        }
        return recipes;
    }

    @SneakyThrows
    private static void createFolders(){
        if(!crafts.isDirectory()){
            Path path = crafts.toPath();
            Files.createDirectories(path);
        }
    }

    private static void disableCrafts(){
        for(String craft : Mauris.getInstance().getConfig().getStringList("crafts.disabledCrafts")){
            Material material = Material.matchMaterial(craft);
            if(material == null) continue;

            disableCraft(new ItemStack(material));
        }
    }

    public static void disableCraft(ItemStack itemStack){
        Bukkit.removeRecipe(itemStack.getType().getKey());
    }

    public static void unloadCrafts(){

        for(MaurisRecipe recipe : loadedRecipes){
            Bukkit.removeRecipe(new NamespacedKey(Mauris.getInstance(), recipe.getName()));
        }

    }

    public static boolean isLoaded(String craft){
        boolean isLoaded = false;
        for(MaurisRecipe recipe : loadedRecipes){
            if (craft.equals(recipe.getName())) {
                isLoaded = true;
                break;
            }
        }

        return isLoaded;
    }

    private static void loadCrafts(){
        File[] craftFiles = crafts.listFiles();
        if(craftFiles == null) return;

        for(File craftfile : craftFiles){

            FileConfiguration craft_yml = YamlConfiguration.loadConfiguration(craftfile);
            ConfigurationSection craftsSection = craft_yml.getConfigurationSection("");
            if(craftsSection == null) continue;

            for(String craftN : craftsSection.getKeys(false)){

                ConfigurationSection s = craft_yml.getConfigurationSection(craftN);
                if(s == null) continue;

                boolean enabled = s.getBoolean("enabled", true);
                if(!enabled) continue;

                MaurisRecipe recipe = getFRPRecipeFromYAML(craftN, s);
                if(recipe == null){
                    Mauris.getMLogger().warning("Cannot load craft" + craftN + " from " + craftfile.getName());
                    continue;
                }
                recipe.setFile(craftfile);

                loadCraft(recipe);
            }
        }

        Mauris.getMLogger().info("✔ Loaded " + loadedRecipes.size() + " recipes");
    }

    public static void loadCraft(MaurisRecipe recipe){

        try{
            Bukkit.getServer().addRecipe(recipe.getRecipe());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        ItemStack is = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        KnowledgeBookMeta bm = (KnowledgeBookMeta) is.getItemMeta();

        NamespacedKey key = new NamespacedKey(Mauris.getInstance(), recipe.getName());
        bm.addRecipe(key);

        DiscoverManager.addToQueue(recipe.getDiscoverBy(), key);
        loadedRecipes.add(recipe);
    }

    public static MaurisRecipe getRecipeByName(String name){
        for(MaurisRecipe recipe : getLoadedRecipes()){
            if(recipe.getName().equalsIgnoreCase(name)){
                return recipe;
            }
        }

        return null;
    }

    public static MaurisRecipe getFRPRecipeFromYAML(String craftN, ConfigurationSection s) {
        MaurisRecipe mRecipe = new MaurisRecipe();
        mRecipe.setName(craftN);

        String typeS = s.getString("type");
        if(typeS == null) return null;
        typeS = typeS.toUpperCase(Locale.ROOT);

        RecipeType type = RecipeTypeManager.getFromName(typeS);
        if(type == null){
            Mauris.getMLogger().warning("Type " + typeS + " from " + craftN + " does not exists");
            return null;
        }

        String resultS = s.getString("result");

        if(resultS == null) {
            Mauris.getMLogger().warning("Result from " + craftN + " is null");
            return null;
        }

        int resultAmount = s.getInt("resultAmount", 1);

        ItemStack result = ItemsLoader.getMaurisItem(resultS, true);

        if(result == null) {
            Mauris.getMLogger().warning("Result " + resultS + " from " + craftN + " does not exists");
            return null;
        }
        result.setAmount(resultAmount);

        String discoverByS = s.getString("discoverBy");
        if(discoverByS == null) {
            discoverByS = "playerjoin";
        }
        discoverByS = discoverByS.toUpperCase();
        mRecipe.setDiscoverBy(discoverByS);

        String group = s.getString("group");

        boolean onlyItemStacks = s.getBoolean("onlyItemStacks", default_onlyItemStacks);

        List<String> returnItems = s.getStringList("returnItems");
        mRecipe.addItems(returnItems);

        NamespacedKey namespace = new NamespacedKey(Mauris.getInstance(), mRecipe.getName());

        ConfigurationSection craftCS = s.getConfigurationSection("craft");
        if(craftCS == null) {
            Mauris.getMLogger().warning("CraftCS " + craftCS.getName() + " is null");
            return null;
        }

        RecipePreloadInformation rpi = new RecipePreloadInformation(result, craftCS, namespace, group, onlyItemStacks);

        Recipe recipe = type.loadFromConfigurationSection(rpi);
        if(recipe == null) return null;

        mRecipe.setRecipe(recipe);

        return mRecipe;
    }



}
