package me.shiqui.simpleteleport.commands;


import me.shiqui.simpleteleport.utils.TeleportHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player)sender;
            TeleportHelper.teleportHome(player);
        }

        return true;

    }
}
