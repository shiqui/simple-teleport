package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerTeleportRequestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player)sender;

            if (args.length != 1) {
                String msg = MessageHelper.stringFromConfig("player.msg.invalid-input");
                player.sendMessage(msg);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                String msg = MessageHelper.stringFromConfig("player.msg.no-target");
                player.sendMessage(msg);
                return true;
            }

            int CD = DatabaseHelper.getPlayerCoolDown(player);
            if (CD > 0) {
                String msg = MessageHelper.stringFromConfig("player.msg.cd");
                msg = msg.replace("<cooldown>", String.valueOf(CD));
                player.sendMessage(msg);
                return true;
            }

            if (target == player) {
                String msg = MessageHelper.stringFromConfig("player.msg.self-tp");
                player.sendMessage(msg);
                return true;
            }

            if (DatabaseHelper.queryPendingOutRequest(player.getUniqueId()) != null) {
                String msg = MessageHelper.stringFromConfig("player.msg.pending-outgoing");
                player.sendMessage(msg);
                return true;
            }

            if (DatabaseHelper.queryPendingInRequest(target.getUniqueId()) != null) {
                String msg = MessageHelper.stringFromConfig("player.msg.busy");
                msg = msg.replace("<player>", target.getName());
                player.sendMessage(msg);
                return true;
            }

            // All check passed, add to pending request
            DatabaseHelper.insertTeleportRequest(player.getUniqueId(), target.getUniqueId(), System.currentTimeMillis());
            String msg = MessageHelper
                .stringFromConfig("player.msg.request.sender")
                .replace("<player>", target.getName());
            player.sendMessage(msg);
            msg = MessageHelper
                .stringFromConfig("player.msg.request.receiver")
                .replace("<player>", player.getName());
            target.sendMessage(msg);

        }

        return true;

    }
}
