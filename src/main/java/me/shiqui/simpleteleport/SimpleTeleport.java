package me.shiqui.simpleteleport;

import me.shiqui.simpleteleport.commands.*;
import me.shiqui.simpleteleport.listeners.LogOutListener;
import me.shiqui.simpleteleport.tasks.ClearExpiredRequestTask;
import me.shiqui.simpleteleport.utils.DatabaseHelper;
import org.bukkit.plugin.java.JavaPlugin;


public final class SimpleTeleport extends JavaPlugin {
    public static SimpleTeleport plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();
        DatabaseHelper.initialize("jdbc:sqlite:" + this.getDataFolder() + "/SimpleTP.db");

        // Register commands
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("home").setExecutor(new HomeCommand());

        getCommand("tpr").setExecutor(new TeleportRequestCommand());
        getCommand("tpa").setExecutor(new TeleportAcceptCommand());
        getCommand("tpd").setExecutor(new TeleportDenyCommand());

        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());


        // Register events
        getServer().getPluginManager().registerEvents(new LogOutListener(), this);

        // Run tasks
        new ClearExpiredRequestTask().runTaskTimer(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Unloading SimpleTeleport");
        DatabaseHelper.disconnect();
    }


}
