package me.shiqui.simpleteleport.utils;

import me.shiqui.simpleteleport.SimpleTeleport;
import me.shiqui.simpleteleport.tasks.TeleportTask;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.sql.SQLException;


public class TeleportHelper{

    private static int getHomeCoolDown(Player player){
        long current = System.currentTimeMillis();
        long previous = DatabaseHelper.queryLastHomeTeleport(player.getUniqueId());
        if (previous == 0) {
            return 0;
        } else {
            long elapsed = current - previous;
            return (SimpleTeleport.plugin.getConfig().getInt("home.cd") - (int) (elapsed / 1000));
        }
    }

    public static void teleportHome(Player player) {
        try {
            Location home = DatabaseHelper.queryHome(player.getUniqueId());

            int CD = getHomeCoolDown(player);
            if (CD <= 0){
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


    public static void teleportWarp(Player player, String warp) {

    }

    public static void teleportPlayer(Player player, Player target) {

    }


}
