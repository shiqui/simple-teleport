package me.shiqui.simpleteleport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class SimpleTeleport extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Loading SimpleTeleport");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Unloading SimpleTeleport");

    }
}
