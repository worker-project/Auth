package com.workerai.authentication.database;

import java.sql.*;

public class Database {
    static Connection conn = null;

    static void initDatabase() {
        try {
            String url = "jdbc:sqlite:data.sqlite";

            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            String sql = "CREATE TABLE IF NOT EXISTS `users` (`id` integer primary key ,`username` VARCHAR(16) NULL, `uuid` VARCHAR(36) NOT NULL ,`discordId` VARCHAR(18) NOT NULL ,`token` VARCHAR(32) NOT NULL ,`automine` INTEGER NOT NULL ,`foraging` INTEGER NOT NULL);";

            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static boolean addAccount(Account account) {
        try {
            String sql = "INSERT INTO users (username, uuid, discordId, token, automine, foraging) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getUuid());
            stmt.setString(3, account.getDiscordId());
            stmt.setString(4, account.getToken());
            stmt.setInt(5, account.hasAutomine() ? 1 : 0);
            stmt.setInt(6, account.hasForaging() ? 1 : 0);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static Account getAccount(String uuid) {
        try {
            String sql = "SELECT * FROM users WHERE uuid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            Account account = new Account();
            account.setUsername(rs.getString("username"));
            account.setUuid(rs.getString("uuid"));
            account.setDiscordId(rs.getString("discordId"));
            account.setToken(rs.getString("token"));
            account.setAutomine((rs.getInt("automine") == 1));
            account.setForaging((rs.getInt("foraging") == 1));
            return account;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    static String getAccountToken(String uuid) {
        try {
            String sql = "SELECT * FROM users WHERE uuid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            return rs.getString("token");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
