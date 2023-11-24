package me.shiqui.simpleteleport.listeners;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import me.shiqui.simpleteleport.utils.MessageHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;


public class LogOutListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        // Remove outgoing request
        Player target = DatabaseHelper.queryPendingOutRequest(playerId);
        DatabaseHelper.removeTeleportRequest(playerId);
        if (target != null) {
            target.sendMessage(MessageHelper.stringFromConfig("player.msg.expire.receiver"));
        }

    }
}
