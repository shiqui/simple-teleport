package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.SimpleTeleport;
import me.shiqui.simpleteleport.tasks.TeleportTask;
import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }
        Player target = (Player)sender;
        Player origin = DatabaseHelper.queryPendingInRequest(target.getUniqueId());

        if (origin == null) {
            target.sendMessage(MessageHelper.stringFromConfig("tpa.error.no-pending"));
            return true;
        }

        DatabaseHelper.insertLastPlayerTeleport(origin.getUniqueId(), System.currentTimeMillis());
        DatabaseHelper.removeTeleportRequest(origin.getUniqueId());

        target.sendMessage(
            MessageHelper
                .stringFromConfig("tpa.msg.sender")
                .replace("<player>", origin.getName())
        );

        origin.sendMessage(
                MessageHelper
                        .stringFromConfig("tpa.msg.receiver")
                        .replace("<player>", target.getName())
        );

        new TeleportTask(
            origin,
            target.getLocation(),
            MessageHelper
                .stringFromConfig("tpr.bossbar")
                .replace("<player>", target.getName())
        ).runTaskTimer(SimpleTeleport.plugin, 0, 1);

        return true;

    }
}
