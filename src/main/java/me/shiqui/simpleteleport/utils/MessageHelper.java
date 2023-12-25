package me.shiqui.simpleteleport.utils;

import me.shiqui.simpleteleport.SimpleTeleport;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class MessageHelper {
    public static String stringFromConfig(String path){
        FileConfiguration config = SimpleTeleport.plugin.getConfig();
        String msg = config.getString("prefix") + " " + config.getString(path);
        return Objects.requireNonNull(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
