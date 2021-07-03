package com.ruverq.mauris;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.fabric.xmlrpc.base.Array;
import com.ruverq.mauris.items.MaurisFolder;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.core.util.FileUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    static String dataPath = Mauris.getInstance().getDataFolder() + File.separator + "data" + File.separator;

    public static void setUp(){

        createFolder("mauris");
        createFolder("mauris/namespace");

        createFolder("resource_pack/assets/minecraft");
        createFile("ids.yml", "");
    }

    public static void removeId(String name, String path){

        File file = getFile("ids.yml");
        YamlConfiguration ys = YamlConfiguration.loadConfiguration(file);

        String namespace = name;

        ConfigurationSection rightCS = ys.getConfigurationSection(path);
        rightCS.set(name, null);

        try {
            ys.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void removeId(MaurisFolder folder, String name, String path){

        File file = getFile("ids.yml");
        YamlConfiguration ys = YamlConfiguration.loadConfiguration(file);

        String namespace = folder.getName() + ":" + name;

        ConfigurationSection rightCS = ys.getConfigurationSection(path);
        rightCS.set(name, null);

        try {
            ys.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int getId(MaurisFolder folder, String name, String path){

        YamlConfiguration ys = YamlConfiguration.loadConfiguration(getFile("ids.yml"));

        String namespace = folder.getName() + ":" + name;

        ConfigurationSection rightCS = ys.getConfigurationSection(path);

        if (rightCS != null && rightCS.contains(namespace)) {
            return rightCS.getInt(namespace);
        }

        return -1;

    }

    private static int generateNewId(ConfigurationSection rightCS){
        List<Integer> ints = new ArrayList<>();
        for(String bruh : rightCS.getKeys(false)){
            ints.add(rightCS.getInt(bruh));
        }

        int i = 1;
        while(ints.contains(i)){
            i++;
        }

        return i;
    }

    public static int addId(MaurisFolder folder, String name, String path){
        int tempId = getId(folder, name, path);
        if(tempId >= 0){
            return tempId;
        }

        File file = getFile("ids.yml");

        YamlConfiguration ys = YamlConfiguration.loadConfiguration(file);

        String namespace = folder.getName() + ":" + name;

        ConfigurationSection rightCS = ys.getConfigurationSection(path);

        if(rightCS == null) {
            createSections(ys, path);
            rightCS = ys.getConfigurationSection(path);
        }else{
            if(rightCS.contains(namespace)) return rightCS.getInt(namespace);
        }

        int newId = generateNewId(rightCS);
        ys.set(path + "." + namespace, newId);

        try {
            ys.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return newId;
    }

    private static YamlConfiguration createSections(YamlConfiguration config, String path){
        StringBuilder sb = new StringBuilder();

        String[] paths = path.split("\\.");
        for(String p : paths){
            sb.append(p);
            if(!config.isConfigurationSection(sb.toString())) config.createSection(sb.toString());
            sb.append('.');
        }

        return config;
    }

    public static JsonObject FileToJson(File file){
        JsonParser parser = new JsonParser();
        JsonElement je = parser.parse(readFile(file));
        if(je == null) return null;
        return je.getAsJsonObject();
    }

    @SneakyThrows
    private static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        List<String> read = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        for(String r : read){
            sb.append(r);
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }



    public static ConfigurationSection getFileAsYAML(String path){
        String tempPath = path.replace("/", File.separator);
        File file = new File(dataPath + tempPath);
        ConfigurationSection cs = YamlConfiguration.loadConfiguration(file);
        return cs;
    }

    public static File getDir(String path){
        String tempPath = path.replace("/", File.separator);
        File file = new File(dataPath + tempPath);
        if(!file.isDirectory() || !file.exists()){
            return null;
        }
        return file;
    }

    @SneakyThrows
    public static File updateFile(String path, String newValue){
        deleteFile(path);
        File file = createFile(path, newValue);

        return file;
    }

    @SneakyThrows
    public static File createFile(String path){
        String tempPath = path.replace("/", File.separator);
        File file = new File(dataPath + tempPath);
        if(file.exists()) return file;
        file.createNewFile();
        return file;
    }

    @SneakyThrows
    public static void deleteFile(String path){
        String tempPath = path.replace("/", File.separator);
        File file = new File(dataPath + tempPath);
        if(file.exists()) file.delete();
    }

    public static File createFile(String path, String value){
        try {
            File file = getFile(path);
            if(file != null) return null;
            file = createFile(path);
                FileOutputStream fos = new FileOutputStream(file);

                BufferedWriter bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(fos, StandardCharsets.UTF_8));
                bufferedWriter.write(value);
                bufferedWriter.close();

            return file;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isFileExists(String path){
        String tempPath = path.replace("/", File.separator);
        File file = new File(dataPath + tempPath);
        return file.exists();
    }

    public static File getFile(String path){
        String tempPath = path.replace("/", File.separator);
        File file = new File(dataPath + tempPath);
        if(file.exists()) return file;
        return null;
    }

    public static void createFolder(String path){
        String tempPath = path.replace("/", File.separator);
        File dir = new File(dataPath + tempPath);
        if(!dir.exists()) dir.mkdirs();
    }

}
