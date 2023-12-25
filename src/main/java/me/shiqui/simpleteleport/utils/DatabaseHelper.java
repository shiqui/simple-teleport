package me.shiqui.simpleteleport.utils;

import me.shiqui.simpleteleport.SimpleTeleport;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper {
    private static Connection conn;

    public static void initialize(String url) {
        try {
            SimpleTeleport.plugin.getLogger().info("Connecting to SQLite");
            DatabaseHelper.conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning(e.getMessage());
        }

        createTables();
    }

    public static void disconnect(){
        try {
            if (DatabaseHelper.conn != null) {
                DatabaseHelper.conn.close();
                SimpleTeleport.plugin.getLogger().info("Disconnecting from SQLite");
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning(e.getMessage());
        }
    }

    public static void createTables(){
        String sql;

        sql = "CREATE TABLE IF NOT EXISTS Homes (\n"
            + "uuid NCHAR(36) PRIMARY KEY,\n"
            + "world NCHAR,\n"
            + "x DOUBLE,\n"
            + "y DOUBLE,\n"
            + "z DOUBLE,\n"
            + "yaw FLOAT,\n"
            + "pitch FLOAT\n"
            + ");";
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] creating Homes" + e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS Warps(\n"
            + "name NCHAR PRIMARY KEY,\n"
            + "world NCHAR,\n"
            + "x DOUBLE,\n"
            + "y DOUBLE,\n"
            + "z DOUBLE,\n"
            + "yaw FLOAT,\n"
            + "pitch FLOAT\n"
            + ");";
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] creating Warps" + e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS LastTeleports(\n"
            + "uuid NCHAR(36) PRIMARY KEY,\n"
            + "home INTEGER DEFAULT 0,\n"
            + "warp INTEGER DEFAULT 0,\n"
            + "player INTEGER DEFAULT 0\n"
            + ");";
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] creating LastTeleports" + e.getMessage());
        }


        sql = "CREATE TABLE IF NOT EXISTS TeleportRequests(\n"
                + "uuid NCHAR(36) PRIMARY KEY,\n"
                + "target NCHAR(36),\n"
                + "createdAt INTEGER DEFAULT 0\n"
                + ");";
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] creating TeleportRequests" + e.getMessage());
        }


    }

    public static int getHomeCoolDown(Player player){
        long current = System.currentTimeMillis();
        long previous = queryLastHomeTeleport(player.getUniqueId());
        if (previous == 0) {
            return 0;
        } else {
            long elapsed = current - previous;
            return (SimpleTeleport.plugin.getConfig().getInt("home.cd") - (int) (elapsed / 1000));
        }
    }

    public static int getPlayerCoolDown(Player player){
        long current = System.currentTimeMillis();
        long previous = queryLastPlayerTeleport(player.getUniqueId());
        if (previous == 0) {
            return 0;
        } else {
            long elapsed = current - previous;
            return (SimpleTeleport.plugin.getConfig().getInt("tpr.cd") - (int) (elapsed / 1000));
        }
    }

    public static int getWarpCoolDown(Player player){
        long current = System.currentTimeMillis();
        long previous = queryLastWarpTeleport(player.getUniqueId());
        if (previous == 0) {
            return 0;
        } else {
            long elapsed = current - previous;
            return (SimpleTeleport.plugin.getConfig().getInt("warp.cd") - (int) (elapsed / 1000));
        }
    }

    public static void insertHome(UUID uuid, String world, double x, double y, double z, float yaw, float pitch) {
        String sql = "INSERT OR REPLACE INTO Homes VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, world);
            statement.setDouble(3, x);
            statement.setDouble(4, y);
            statement.setDouble(5, z);
            statement.setFloat(6, yaw);
            statement.setFloat(7, pitch);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] inserting into Homes: " + e.getMessage());
        }
    }

    public static void insertWarp(String name, String world, double x, double y, double z, float yaw, float pitch) {
        String sql = "INSERT OR REPLACE INTO Warps VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, world);
            statement.setDouble(3, x);
            statement.setDouble(4, y);
            statement.setDouble(5, z);
            statement.setFloat(6, yaw);
            statement.setFloat(7, pitch);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] inserting into Warps: " + e.getMessage());
        }
    }

    public static void insertLastHomeTeleport(UUID uuid, long timestamp){
        String sqlInsert = "INSERT OR IGNORE INTO LastTeleports (uuid, home) VALUES(?, ?)";
        String sqlUpdate = "UPDATE LastTeleports SET home = ? WHERE uuid = ?";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsert)) {
            statement.setString(1, uuid.toString());
            statement.setLong(2, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] inserting home into LastTeleports: " + e.getMessage());
        }

        try (PreparedStatement statement = conn.prepareStatement(sqlUpdate)) {
            statement.setLong(1, timestamp);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] updating home in LastTeleports: " + e.getMessage());
        }
    }

    public static void insertLastWarpTeleport(UUID uuid, long timestamp){
        String sqlInsert = "INSERT OR IGNORE INTO LastTeleports (uuid, warp) VALUES(?, ?)";
        String sqlUpdate = "UPDATE LastTeleports SET warp = ? WHERE uuid = ?";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsert)) {
            statement.setString(1, uuid.toString());
            statement.setLong(2, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] inserting warp into LastTeleports: " + e.getMessage());
        }

        try (PreparedStatement statement = conn.prepareStatement(sqlUpdate)) {
            statement.setLong(1, timestamp);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] updating warp in LastTeleports: " + e.getMessage());
        }
    }

    public static void insertLastPlayerTeleport(UUID uuid, long timestamp){
        String sqlInsert = "INSERT OR IGNORE INTO LastTeleports (uuid, player) VALUES(?, ?)";
        String sqlUpdate = "UPDATE LastTeleports SET player = ? WHERE uuid = ?";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsert)) {
            statement.setString(1, uuid.toString());
            statement.setLong(2, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] inserting player into LastTeleports: " + e.getMessage());
        }

        try (PreparedStatement statement = conn.prepareStatement(sqlUpdate)) {
            statement.setLong(1, timestamp);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] updating player in LastTeleports: " + e.getMessage());
        }
    }

    public static void insertTeleportRequest(UUID uuid, UUID targetID, long timestamp){
        String sql = "INSERT OR IGNORE INTO TeleportRequests (uuid, target, createdAt) VALUES(?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, targetID.toString());
            statement.setLong(3, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SimpleTeleport.plugin.getLogger().warning("[SQLite] inserting player into TeleportRequests: " + e.getMessage());
        }

    }

    public static Location queryHome(UUID uuid){
        String sql = "SELECT world, x, y, z, yaw, pitch FROM Homes WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Location(
                            SimpleTeleport.plugin.getServer().getWorld(resultSet.getString("world")),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"),
                            resultSet.getFloat("yaw"),
                            resultSet.getFloat("pitch")
                    );
                }
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying from Homes: " + e.getMessage());
        }
        return null;
    }

    public static Location queryWarp(String name) {
        String sql = "SELECT world, x, y, z, yaw, pitch FROM Warps WHERE name = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Location(
                            SimpleTeleport.plugin.getServer().getWorld(resultSet.getString("world")),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"),
                            resultSet.getFloat("yaw"),
                            resultSet.getFloat("pitch")
                    );
                }
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying from Warps: " + e.getMessage());
        }
        return null;
    }

    public static List<String> queryAllWarpNames() {
        String sql = "SELECT name FROM Warps";
        try (
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
            ) {
            List<String> warpNames = new ArrayList<>();
            while (resultSet.next()) {
                warpNames.add(resultSet.getString("name"));
            }
            return warpNames;
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying from Warps: " + e.getMessage());
        }
        return null;
    }

    public static long queryLastHomeTeleport(UUID uuid) {
        String sql = "SELECT home FROM LastTeleports WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("home");
                }
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying home from LastTeleports: " + e.getMessage());
        }
        return 0L;
    }

    public static long queryLastWarpTeleport(UUID uuid) {
        String sql = "SELECT warp FROM LastTeleports WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("warp");
                }
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying warp from LastTeleports: " + e.getMessage());
        }
        return 0L;
    }

    public static long queryLastPlayerTeleport(UUID uuid) {
        String sql = "SELECT player FROM LastTeleports WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("player");
                }
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying player from LastTeleports: " + e.getMessage());
        }
        return 0L;
    }

    public static Player queryPendingInRequest(UUID uuid) {
        String sql = "SELECT * FROM TeleportRequests WHERE target = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Bukkit.getPlayer(UUID.fromString(resultSet.getString("uuid")));
                }
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying from TeleportRequests: " + e.getMessage());
        }
        return null;
    }

    public static Player queryPendingOutRequest(UUID uuid) {
        String sql = "SELECT * FROM TeleportRequests WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Bukkit.getPlayer(UUID.fromString(resultSet.getString("target")));
                }
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] querying from TeleportRequests: " + e.getMessage());
        }
        return null;
    }

    public static ArrayList<UUID> queryExpiredRequest() {
        long current = System.currentTimeMillis();
        long threshold = current - (SimpleTeleport.plugin.getConfig().getInt("tpr.expire-time") * 1000L);
        String sql = "SELECT * FROM TeleportRequests WHERE createdAt < ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, threshold);
            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<UUID> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(UUID.fromString(resultSet.getString("uuid")));
                }
                return result;
            }
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] deleting from TeleportRequests: " + e.getMessage());
        }
        return null;
    }
    public static void removeTeleportRequest(UUID uuid) {
        String sql = "DELETE FROM TeleportRequests WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] deleting from TeleportRequests: " + e.getMessage());
        }
    }

    public static void removeWarp(String name) {
        String sql = "DELETE FROM Warps WHERE name = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            SimpleTeleport.plugin.getLogger().warning("[SQLite] deleting from Warps: " + e.getMessage());
        }
    }

}


