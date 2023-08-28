package me.shiqui.simpleteleport.tasks;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTask extends BukkitRunnable {
    private Player player;
    private Location destination;
    private BossBar bossBar;
    private double progress = 0.0;

    public TeleportTask(Player player, Location destination, String msg){
        this.player = player;
        this.destination = destination;
        this.bossBar = Bukkit.getServer().createBossBar(
                msg,
                BarColor.PURPLE,
                BarStyle.SOLID);
        bossBar.addPlayer(player);
    }

    @Override
    public void run() {

        if(progress == 0) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1, 1);
        } else if (progress >= 0.99) {
            player.teleport(destination);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            bossBar.removePlayer(player);
            cancel();
        }

        bossBar.setProgress(progress);
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH,player.getLocation(),8);
        progress += 0.01;
    }

}
