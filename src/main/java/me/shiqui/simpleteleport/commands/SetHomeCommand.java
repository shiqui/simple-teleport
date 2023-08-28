package me.shiqui.simpleteleport.commands;

import me.shiqui.simpleteleport.SimpleTeleport;
import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){

            Player player = (Player)sender;
            Location location = player.getLocation();
            UUID uuid = player.getUniqueId();

            DatabaseHelper.insertHome(uuid, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

            String x = String.valueOf(Math.round(location.getX()));
            String y = String.valueOf(Math.round(location.getY()));
            String z = String.valueOf(Math.round(location.getZ()));

            String msg = MessageHelper.stringFromConfig("sethome.msg")
                    .replace("<x>", x)
                    .replace("<y>", y)
                    .replace("<z>", z);

            player.sendMessage(msg);

        }
        return true;

    }

}
