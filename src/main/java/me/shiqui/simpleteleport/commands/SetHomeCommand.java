package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }

        Player player = (Player)sender;
        if (args.length != 0) {
            player.sendMessage(MessageHelper.stringFromConfig("sethome.error.invalid-syntax"));
            return true;
        }
        
        Location location = player.getLocation();

        DatabaseHelper.insertHome(
            player.getUniqueId(),
            Objects.requireNonNull(location.getWorld()).getName(),
            location.getX(),
            location.getY(),
            location.getZ(),
            location.getYaw(),
            location.getPitch()
        );

        player.sendMessage(
            MessageHelper
                .stringFromConfig("sethome.msg")
                .replace("<x>", String.valueOf(Math.round(location.getX())))
                .replace("<y>", String.valueOf(Math.round(location.getY())))
                .replace("<z>", String.valueOf(Math.round(location.getZ())))
        );

        return true;
    }
}
