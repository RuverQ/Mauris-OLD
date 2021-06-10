package com.ruverq.mauris.commands;

import com.ruverq.mauris.Mauris;
import com.ruverq.mauris.ResourcePackHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RPZipCommand implements SimpleCommand{

    @Override
    public String name() {
        return "zip";
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

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
}
