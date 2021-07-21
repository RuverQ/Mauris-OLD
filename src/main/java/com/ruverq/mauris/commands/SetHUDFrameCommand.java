package com.ruverq.mauris.commands;

import com.ruverq.mauris.items.hud.PlayerHUDInfoManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHUDFrameCommand implements SimpleCommand{
    @Override
    public String name() {
        return "setframe";
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String prefix, String[] args) {

        Player p = (Player) sender;

        if(args.length < 3){
            sender.sendMessage(prefix + "Please provide hud name and frame number");
            return;
        }

        String hud = args[1];
        String frameS = args[2];

        if(!StringUtils.isNumeric(frameS)){
            sender.sendMessage(prefix + "That's not number");
            return;
        }

        int frame = Integer.parseInt(frameS);

        PlayerHUDInfoManager.getInfo(p).setFrame(hud, frame);
        sender.sendMessage(prefix + "Executed!");
    }
}
