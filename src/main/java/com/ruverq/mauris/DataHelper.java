package com.ruverq.mauris;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruverq.mauris.items.MaurisFolder;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class DataHelper {

    static String dataPath = Mauris.getInstance().getDataFolder() + File.separator + "data" + File.separator;

    public static void setUp(){

        createFolder("mauris");
        createFolder("mauris/namespace");

        createFolder("resource_pack/assets/minecraft");
        createFile("ids.yml", "");
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

        int newId = rightCS.getKeys(false).size() + 1;

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
            config.createSection(sb.toString());
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

    private static String readFile(File file) {
        try{

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;
                StringBuilder stringBuilder = new StringBuilder();
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                return stringBuilder.toString();
            }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
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

            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(value);
            printWriter.close();

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
