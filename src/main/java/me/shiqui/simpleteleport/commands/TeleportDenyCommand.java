package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportDenyCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }

        Player target = (Player)sender;
        if (args.length != 0) {
            target.sendMessage(MessageHelper.stringFromConfig("tpd.error.invalid-syntax"));
            return true;
        }

        Player origin = DatabaseHelper.queryPendingInRequest(target.getUniqueId());
        if (origin == null) {
            target.sendMessage(MessageHelper.stringFromConfig("tpd.error.no-pending"));
            return true;
        }
        
        DatabaseHelper.insertLastPlayerTeleport(origin.getUniqueId(), System.currentTimeMillis());
        DatabaseHelper.removeTeleportRequest(origin.getUniqueId());

        target.sendMessage(
                MessageHelper
                        .stringFromConfig("tpd.msg.sender")
                        .replace("<player>", origin.getName())
        );

        origin.sendMessage(
            MessageHelper
                .stringFromConfig("tpd.msg.receiver")
                .replace("<player>", target.getName())
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<String>();
    }
}
