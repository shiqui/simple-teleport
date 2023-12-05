package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.SimpleTeleport;
import me.shiqui.simpleteleport.tasks.TeleportTask;
import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.shiqui.simpleteleport.utils.DatabaseHelper.queryWarp;

public class WarpCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){ return true; }

        Player player = (Player)sender;
        if (args.length != 1) {
            player.sendMessage(MessageHelper.stringFromConfig("warp.error.invalid-syntax"));
            return true;
        }

        Location warp = queryWarp(args[0]);
        if (warp == null) {
            player.sendMessage(MessageHelper.stringFromConfig("warp.error.no-warp"));
            return true;
        }

        int CD = DatabaseHelper.getWarpCoolDown(player);
        if (CD > 0) {
            player.sendMessage(
                MessageHelper
                    .stringFromConfig("warp.error.cd")
                    .replace("<cooldown>", String.valueOf(CD))
            );
            return true;
        }

        // All check passed, proceed to warp

        player.sendMessage(
            MessageHelper
                .stringFromConfig("warp.msg")
                .replace("<warp>", args[0])
        );

        DatabaseHelper.insertLastWarpTeleport(player.getUniqueId(), System.currentTimeMillis());

        new TeleportTask(
            player,
            warp,
            MessageHelper
                .stringFromConfig("warp.bossbar")
                .replace("<warp>", args[0])
        ).runTaskTimer(SimpleTeleport.plugin, 0, 1);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) { return DatabaseHelper.queryAllWarpNames(); }
        return new ArrayList<String>();
    }
}
