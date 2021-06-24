package com.ruverq.mauris;

import com.google.gson.JsonObject;
import com.ruverq.mauris.utils.ZipUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import net.lingala.zip4j.ZipFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class ResourcePackHelper implements Listener {

    static int port = 8232;

    static String ip = "localhost";
    static String randomUUID;
    static String url = "http://" + ip + ":" + port + "/rp.zip";
    boolean isHosted;
    boolean isZiped;

    static HttpServer server;


    public void sendTo(Player player){
        player.setResourcePack(url);
    }

    @SneakyThrows
    public void zipResourcePack(){
        File file = DataHelper.getDir("resource_pack");
        File rpzip = new File(file.getAbsolutePath() + File.separator + "rp.zip");
        rpzip.delete();

        generateMCMETA();

        ZipFile zipFile = new ZipFile(file.getAbsolutePath() + File.separator + "rp.zip");
        zipFile.addFile(file.getAbsoluteFile() + File.separator + "pack.png");
        zipFile.addFile(file.getAbsoluteFile() + File.separator + "pack.mcmeta");
        zipFile.addFolder(DataHelper.getDir("resource_pack/assets"));

        isZiped = true;
    }

    public void generateMCMETA(){
        DataHelper.deleteFile("resource_pack/pack.mcmeta");

        JsonObject jsonObject = new JsonObject();
        JsonObject packObject = new JsonObject();
        packObject.addProperty("description", new Date(System.currentTimeMillis()).toString());
        packObject.addProperty("pack_format", 6);

        jsonObject.add("pack", packObject);

        DataHelper.createFile("resource_pack/pack.mcmeta", jsonObject.toString());
    }

    @SneakyThrows
    public void hostResourcePack(){
        File file = DataHelper.getDir("resource_pack");
        if(server != null){
            server.stop(0);
            server = null;
        }

        randomUUID = UUID.randomUUID().toString();
        url = "http://" + ip + ":" + port + "/rp.zip#" + randomUUID;

        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/rp.zip", new HHandler(file.getPath() + File.separator + "rp.zip"));
        server.setExecutor(null);

        server.start();

        //System.out.println("Hosted on " + url);
        isHosted = true;
    }

    static class HHandler implements HttpHandler {

        String path;
        public HHandler(String path) {
            this.path = path;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            Headers h = t.getResponseHeaders();

            File newFile = new File(path);
            byte[] data = Files.readAllBytes(newFile.toPath());

            h.add("Content-Type", "application/zip");
            t.sendResponseHeaders(200, data.length);
            OutputStream os = t.getResponseBody();
            os.write(data);
            os.close();

        }
    }


    public static void setupRP(){

        ResourcePackHelper rph = new ResourcePackHelper();

        rph.zipResourcePack();

        new BukkitRunnable() {
            @Override
            public void run() {

                rph.hostResourcePack();
                for(Player player : Bukkit.getOnlinePlayers()){
                    rph.sendTo(player);
                }

            }
        }.runTaskLater(Mauris.getInstance(), 5);

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.getPlayer().setResourcePack(url);
    }


}
