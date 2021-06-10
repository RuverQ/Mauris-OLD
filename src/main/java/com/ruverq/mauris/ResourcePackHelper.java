package com.ruverq.mauris;

import com.ruverq.mauris.utils.ZipUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class ResourcePackHelper {

    int port = 8090;

    String ip = "127.0.0.1";
    String url = "http://" + ip + ":" + port + "/rp.zip";

    boolean isHosted;
    boolean isZiped;

    static HttpServer server;

    public void sendTo(Player player){
        player.setResourcePack(url);
    }

    public void zipResourcePack(){
        File file = DataHelper.getDir("resource_pack");
        File rpzip = new File(file.getAbsolutePath() + File.separator + "rp.zip");
        rpzip.delete();

        ZipUtils zipUtils = new ZipUtils(file.getAbsolutePath() + File.separator + "rp.zip", file.getAbsolutePath());
        zipUtils.run();

        isZiped = true;
    }

    @SneakyThrows
    public void hostResourcePack(){
        File file = DataHelper.getDir("resource_pack");
        if(server != null){
            server.stop(0);
            server = null;
        }

        server = HttpServer.create(new InetSocketAddress(port), 0);
        String path = file.getPath() + File.separator + "rp.zip";

        server.createContext("/rp.zip", new HHandler(path));
        server.setExecutor(null);

        server.start();

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

    public void generateResourcePack(){

    }

}
