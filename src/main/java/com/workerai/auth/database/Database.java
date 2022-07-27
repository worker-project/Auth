package com.workerai.auth.database;

import com.workerai.auth.WorkerAuth;

import java.sql.*;

public class Database {
    static Connection conn = null;

    static void initDatabase() {
        try {
            String url = "jdbc:sqlite:data.sqlite";

            conn = DriverManager.getConnection(url);

            WorkerAuth.getLogger().custom("initialization", "Connection to SQLite has been established.\n");

            String sql = "CREATE TABLE IF NOT EXISTS `accounts` (" +
                    "`ID`       INTEGER     PRIMARY KEY," +
                    "`USERNAME` CHAR(16)    NULL," +
                    "`UUID`     CHAR(36)    NOT NULL," +
                    "`DISCORD`  CHAR(18)    NOT NULL," +
                    "`TOKEN`    CHAR(32)    NOT NULL," +
                    "`AUTOMINE` INTEGER     NOT NULL," +
                    "`FORAGING` INTEGER     NOT NULL" +
                    ");";

            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static boolean addAccount(Account account) {
        try {
            String sql = "INSERT INTO accounts (USERNAME, UUID, DISCORD, TOKEN, AUTOMINE, FORAGING) VALUES (?, ?, ?, ?, ?, ?)";
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
            String sql = "SELECT * FROM accounts WHERE UUID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            Account account = new Account();
            account.setUsername(rs.getString("USERNAME"));
            account.setUuid(rs.getString("UUID"));
            account.setDiscordId(rs.getString("DISCORD"));
            account.setToken(rs.getString("TOKEN"));
            account.setAutomine((rs.getInt("AUTOMINE") == 1));
            account.setForaging((rs.getInt("FORAGING") == 1));
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    static String getAccountToken(String uuid) {
        try {
            String sql = "SELECT * FROM accounts WHERE UUID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            return rs.getString("TOKEN");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
