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

public class HomeCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }

        Player player = (Player)sender;
        if (args.length != 0) {
            player.sendMessage(MessageHelper.stringFromConfig("home.error.invalid-syntax"));
            return true;
        }

        int CD = DatabaseHelper.getHomeCoolDown(player);
        if (CD > 0) {
            player.sendMessage(
                MessageHelper
                    .stringFromConfig("home.error.cd")
                    .replace("<cooldown>", String.valueOf(CD))
            );
            return true;
        }

        Location home = DatabaseHelper.queryHome(player.getUniqueId());
        if (home == null) {
            player.sendMessage(MessageHelper.stringFromConfig("home.error.no-home"));
            return true;
        }

        player.sendMessage(MessageHelper.stringFromConfig("home.msg"));
        DatabaseHelper.insertLastHomeTeleport(player.getUniqueId(), System.currentTimeMillis());

        new TeleportTask(
            player,
            home,
            MessageHelper.stringFromConfig("home.bossbar")
        ).runTaskTimer(SimpleTeleport.plugin, 0, 1);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<String>();
    }
}
