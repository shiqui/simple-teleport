package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }

        Player player = (Player)sender;
        if (args.length != 1) {
            player.sendMessage(MessageHelper.stringFromConfig("setwarp.error.invalid-syntax"));
            return true;
        }

        String name = args[0];
        Location location = player.getLocation();

        DatabaseHelper.insertWarp(name, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        String x = String.valueOf(Math.round(location.getX()));
        String y = String.valueOf(Math.round(location.getY()));
        String z = String.valueOf(Math.round(location.getZ()));

        player.sendMessage(
            MessageHelper
                .stringFromConfig("setwarp.msg")
                .replace("<warp>", name)
                .replace("<x>", x)
                .replace("<y>", y)
                .replace("<z>", z)
        );

        return true;
    }
}
