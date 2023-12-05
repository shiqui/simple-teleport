package me.shiqui.simpleteleport.commands;

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

public class RemoveWarpCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){ return true; }

        Player player = (Player)sender;
        if (args.length != 1) {
            player.sendMessage(MessageHelper.stringFromConfig("dewarp.error.invalid-syntax"));
            return true;
        }

        if (DatabaseHelper.queryWarp(args[0]) == null) {
            player.sendMessage(MessageHelper.stringFromConfig("dewarp.error.no-warp"));
            return true;
        }

        DatabaseHelper.removeWarp(args[0]);

        player.sendMessage(
            MessageHelper
                .stringFromConfig("dewarp.msg")
                .replace("<warp>", args[0])
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) { return DatabaseHelper.queryAllWarpNames(); }
        return new ArrayList<String>();
    }
}
