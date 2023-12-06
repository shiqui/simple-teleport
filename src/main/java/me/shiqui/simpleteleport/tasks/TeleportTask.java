package me.shiqui.simpleteleport.tasks;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTask extends BukkitRunnable {
    private final Player player;
    private final Location origin;
    private final Location destination;
    private final BossBar bossBar;
    private double progress = 0.0;

    public TeleportTask(Player player, Location destination, String msg){
        this.player = player;
        this.origin = player.getLocation();
        this.destination = destination;
        this.bossBar = Bukkit.getServer().createBossBar(
            msg,
            BarColor.PURPLE,
            BarStyle.SOLID
        );
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

        spawnPortal(origin, 3 - progress * 2);
        spawnSpiral(player.getLocation(), 1);
        spawnPortal(destination, 3 - progress * 2);

        bossBar.setProgress(1 - progress);
        progress += 0.01;
    }

    public void spawnPortal(Location location, double radius) {
        double phase_0 = Math.toRadians(progress * 1500);
        for (int i = 0; i < 5; i ++) {
            double phase = phase_0 + i * 0.2;
            location.getWorld().spawnParticle(
                Particle.REDSTONE,
                location.getX() + radius * Math.cos(-phase),
                location.getY(),
                location.getZ() + radius * Math.sin(-phase),
                1,
                new Particle.DustOptions(Color.fromARGB(200 - i * 5,112, 0, 112), 2)
            );
        }
    }

    public void spawnSpiral(Location location, double radius) {
        double angle = Math.toRadians(progress * 1000);
        for (int i = 0; i < 360; i += 60) {
            location.getWorld().spawnParticle(
                Particle.REDSTONE,
                location.getX() + radius * Math.cos(angle + i),
                location.getY() + 2 - progress * 2,
                location.getZ() + radius * Math.sin(angle + i),
                1,
                new Particle.DustOptions(Color.fromARGB((int) (progress * 50) + 150,180, 90, 180), 1)
            );
        }
    }
}
