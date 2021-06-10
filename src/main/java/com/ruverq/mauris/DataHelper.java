package com.ruverq.mauris;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

import java.io.*;

public class DataHelper {

    static String dataPath = Mauris.getInstance().getDataFolder() + File.separator + "data" + File.separator;

    public static void setUp(){

        createFolder("mauris");
        createFolder("mauris/namespace");

        createFolder("resource_pack/assets/minecraft");
        createFile("ids.json", "{ }");
    }

    public static int getId(String folder, String name){
        String namespace = folder + ":" + name;
        JsonObject jsonObject = FileToJson(getFile("ids.json"));

        JsonElement je = jsonObject.get(namespace);

        if(je == null) return -1;

        return je.getAsInt();
    }

    public static int addId(String folder, String name){
        String namespace = folder + ":" + name;
        JsonObject jsonObject = FileToJson(getFile("ids.json"));
        System.out.println("a");
        if(jsonObject.get(namespace) != null) return jsonObject.get(namespace).getAsInt();
        System.out.println("b");
        int id = jsonObject.entrySet().size() + 1;
        jsonObject.addProperty(namespace, id);
        updateFile("ids.json", jsonObject.toString());
        return id;
    }

    public static JsonObject FileToJson(File file){
        JsonParser parser = new JsonParser();
        JsonElement je = parser.parse(readFile(file));
        if(je == null) return null;
        return je.getAsJsonObject();
    }

    private static String readFile(File file) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String         line = null;
            StringBuilder  stringBuilder = new StringBuilder();
            String         ls = System.getProperty("line.separator");

            try {
                while((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                return stringBuilder.toString();
            } finally {
                reader.close();
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
        System.out.println(file.getAbsolutePath());
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
            File file = createFile(path);

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
