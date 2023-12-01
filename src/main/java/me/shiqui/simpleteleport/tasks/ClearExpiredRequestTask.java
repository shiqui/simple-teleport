package me.shiqui.simpleteleport.tasks;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class ClearExpiredRequestTask extends BukkitRunnable {
    @Override
    public void run() {
        ArrayList<UUID> playerIds = DatabaseHelper.queryExpiredRequest();
        if (playerIds == null) { return; }
        for (UUID playerId : playerIds) {
            Player player = Bukkit.getPlayer(playerId);
            if (player == null) { continue; }
            player.sendMessage(MessageHelper.stringFromConfig("tpr.error.expire"));
            DatabaseHelper.removeTeleportRequest(playerId);
        }
    }
}
