package me.shiqui.simpleteleport;

import me.shiqui.simpleteleport.commands.HomeCommand;
import me.shiqui.simpleteleport.commands.SetHomeCommand;
import me.shiqui.simpleteleport.utils.DatabaseHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;


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

        // test

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Unloading SimpleTeleport");
        DatabaseHelper.disconnect();
    }


}
