package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import me.shiqui.simpleteleport.utils.TeleportHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerTeleportAcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player target = (Player)sender;
            Player origin = DatabaseHelper.queryPendingInRequest(target.getUniqueId());

            if (origin == null) {
                target.sendMessage(MessageHelper.stringFromConfig("player.msg.accept.no-pending"));
                return true;
            }

            // TODO: move getCD to DatabaseHelper because it fetches data from db
            // TODO: move all checks to Command, TeleportHelper should assume valid input and only handle TP
            TeleportHelper.teleportPlayer(origin, target);

            String msg = MessageHelper
                .stringFromConfig("player.msg.accept.sender")
                .replace("<player>", target.getName());
            origin.sendMessage(msg);

            msg = MessageHelper
                .stringFromConfig("player.msg.accept.receiver")
                .replace("<player>", origin.getName());
            target.sendMessage(msg);

        }

        return true;

    }
}
