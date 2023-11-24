package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import me.shiqui.simpleteleport.utils.TeleportHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerTeleportDenyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player target = (Player)sender;
        Player origin = DatabaseHelper.queryPendingInRequest(target.getUniqueId());

        if (origin == null) {
            target.sendMessage(MessageHelper.stringFromConfig("player.msg.deny.no-pending"));
            return true;
        }

        DatabaseHelper.removeTeleportRequest(origin.getUniqueId());

        origin.sendMessage(
            MessageHelper
                .stringFromConfig("player.msg.deny.sender")
                .replace("<player>", target.getName())
        );

        target.sendMessage(
            MessageHelper
                .stringFromConfig("player.msg.deny.receiver")
                .replace("<player>", origin.getName())
        );
        return true;
    }
}
