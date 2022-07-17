package com.workerai.authentication.database;

public class Account {
    public static class AccountData {
        private String username;
        private String uuid;
        private String discordId;
        private String token;
        private boolean automine;
        private boolean foraging;
    }
    public AccountData accountData = new AccountData();

    public String getUsername() { return accountData.username; }
    public void setUsername(String username) {
        accountData.username = username;
    }

    public String getUuid() {
        return accountData.uuid;
    }
    public void setUuid(String uuid) {
        accountData.uuid = uuid;
    }

    public String getDiscordId() {
        return accountData.discordId;
    }
    public void setDiscordId(String discordId) {
        accountData.discordId = discordId;
    }

    public String getToken() {
        return accountData.token;
    }
    public void setToken(String token) {
        accountData.token = token;
    }

    public void setAutomine(boolean automine) {
        accountData.automine = automine;
    }
    public boolean hasAutomine() {
        return accountData.automine;
    }

    public void setForaging(boolean foraging) {
        accountData.foraging = foraging;
    }
    public boolean hasForaging() {
        return accountData.foraging;
    }

    public String displayAccountData() {
        return "\n{\n  USERNAME: " + getUsername() + ",\n  UUID: " + getUuid() + ",\n  TOKEN: " + getToken() + ",\n  AUTOMINE: " + hasAutomine() + ",\n  FORAGING: " + hasForaging() + ",\n}";
    }
}