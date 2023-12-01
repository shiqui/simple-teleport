package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportRequestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }

        Player player = (Player)sender;
        if (args.length != 1) {
            player.sendMessage(MessageHelper.stringFromConfig("tpr.error.invalid-syntax"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(MessageHelper.stringFromConfig("tpr.error.no-target"));
            return true;
        }

        int CD = DatabaseHelper.getPlayerCoolDown(player);
        if (CD > 0) {
            player.sendMessage(
                MessageHelper
                    .stringFromConfig("tpr.error.cd")
                    .replace("<cooldown>", String.valueOf(CD))
            );
            return true;
        }

        if (target == player) {
            player.sendMessage(MessageHelper.stringFromConfig("tpr.error.self-tp"));
            return true;
        }

        if (DatabaseHelper.queryPendingOutRequest(player.getUniqueId()) != null) {
            player.sendMessage(MessageHelper.stringFromConfig("tpr.error.pending-outgoing"));
            return true;
        }

        if (DatabaseHelper.queryPendingInRequest(target.getUniqueId()) != null) {
            player.sendMessage(
                MessageHelper
                    .stringFromConfig("tpr.error.busy")
                    .replace("<player>", target.getName())
            );
            return true;
        }

        // All check passed, add to pending request
        DatabaseHelper.insertTeleportRequest(player.getUniqueId(), target.getUniqueId(), System.currentTimeMillis());

        player.sendMessage(
            MessageHelper
                .stringFromConfig("tpr.msg.sender")
                .replace("<player>", target.getName())
        );

        target.sendMessage(
            MessageHelper
                .stringFromConfig("tpr.msg.receiver")
                .replace("<player>", player.getName())
        );

        return true;
    }
}
