package me.shiqui.simpleteleport.tasks;

import me.shiqui.simpleteleport.utils.DatabaseHelper;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearExpiredRequestTask extends BukkitRunnable {
    @Override
    public void run() {
        DatabaseHelper.removeExpiredTeleportRequest();
    }
}
