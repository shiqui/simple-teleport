package me.shiqui.simpleteleport.utils;

import me.shiqui.simpleteleport.SimpleTeleport;
import me.shiqui.simpleteleport.tasks.TeleportTask;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;


public class TeleportHelper{

    public static void teleportHome(Player player) {
        try {
            Location home = DatabaseHelper.queryHome(player.getUniqueId());

            int CD = DatabaseHelper.getHomeCoolDown(player);
            if (CD <= 0) {
                // Teleport player to home, set CD
                String msg = MessageHelper.stringFromConfig("home.msg.tp");
                String bossBarMsg = MessageHelper.stringFromConfig("home.bossbar");

                player.sendMessage(msg);
                DatabaseHelper.insertLastHomeTeleport(player.getUniqueId(), System.currentTimeMillis());

                new TeleportTask(player, home, bossBarMsg).runTaskTimer(SimpleTeleport.plugin, 0, 1);
            } else {
                // Do nothing, /home still in CD
                String msg = MessageHelper.stringFromConfig("home.msg.cd");
                msg = msg.replace("<cooldown>", String.valueOf(CD));
                player.sendMessage(msg);
            }
        } catch (DatabaseHelper.EmptyQueryException e) {
            // No home found: the player has never used /sethome
            String msg = MessageHelper.stringFromConfig("home.msg.no-home");
            player.sendMessage(msg);
        }
    }


    public static void teleportWarp(Player player, Location warp) {

        DatabaseHelper.insertLastWarpTeleport(player.getUniqueId(), System.currentTimeMillis());

        new TeleportTask(
            player,
            warp,
            MessageHelper.stringFromConfig("warp.bossbar")
        ).runTaskTimer(SimpleTeleport.plugin, 0, 1);

    }

    public static void teleportPlayer(Player player, Player target) {
        int CD = DatabaseHelper.getPlayerCoolDown(player);
        if (CD <= 0) {
            // Teleport player to home, set CD
            String bossBarMsg = MessageHelper
                .stringFromConfig("player.bossbar")
                .replace("<player>", target.getName());

            DatabaseHelper.insertLastPlayerTeleport(player.getUniqueId(), System.currentTimeMillis());
            DatabaseHelper.removeTeleportRequest(player.getUniqueId());

            new TeleportTask(player, target.getLocation(), bossBarMsg).runTaskTimer(SimpleTeleport.plugin, 0, 1);
        } else {
            // Do nothing, /tpr still in CD
            String msg = MessageHelper.stringFromConfig("player.msg.cd");
            msg = msg.replace("<cooldown>", String.valueOf(CD));
            player.sendMessage(msg);
        }
    }


}
