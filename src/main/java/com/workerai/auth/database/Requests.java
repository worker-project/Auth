package com.workerai.auth.database;

import com.google.gson.JsonObject;
import com.workerai.auth.WorkerAuth;
import com.workerai.auth.utils.AccountHelper;
import spark.Spark;

public class Requests {
    public static void initRequests() {
        Database.initDatabase();
        Spark.port(2929);

        Spark.post("/TnkNDZ0vzkmRmi40", (request, response) -> { // getUser with discordID
            WorkerAuth.getLogger().custom("getUserFromDiscord", "SQL request detected!");
            String discord = request.queryParams("discord");

            Account account = Database.getAccountFromDiscord(discord);
            response.type("application/json");
            if (account == null) {
                response.status(200);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("exists", Boolean.FALSE);

                WorkerAuth.getLogger().custom("getUserFromDiscord", "SQL request negative!");
                WorkerAuth.getLogger().custom("getUserFromDiscord", "SQL request report: [DISCORD] \"" + discord + "\" doesn't exist.");
                WorkerAuth.getLogger().custom("getUserFromDiscord", "SQL request return: " + jsonObject + "\n");
                return jsonObject.toString();
            }
            response.status(200);
            JsonObject object = new JsonObject();
            object.addProperty("exists", Boolean.TRUE);
            object.addProperty("automine", account.hasAutomine());
            object.addProperty("forage", account.hasForaging());

            WorkerAuth.getLogger().custom("getUserFromDiscord", "SQL request positive!");
            WorkerAuth.getLogger().custom("getUserFromDiscord", "SQL request report: [DISCORD] \"" + discord + "\" exist.");
            WorkerAuth.getLogger().custom("getUserFromDiscord", "SQL request return: " + object + "\n");
            return object.toString();
        });

        Spark.post("/jWuR0gHff54WvVzL", (request, response) -> { // getUser with uuid
            WorkerAuth.getLogger().custom("getUserFromUuid", "SQL request detected!");
            String uuid = request.queryParams("uuid");

            Account account = Database.getAccountFromUuid(uuid);
            response.type("application/json");
            if (account == null) {
                response.status(200);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("exists", Boolean.FALSE);

                WorkerAuth.getLogger().custom("getUserFromUuid", "SQL request negative!");
                WorkerAuth.getLogger().custom("getUserFromUuid", "SQL request report: [USER] \"" + uuid + "\" doesn't exist.");
                WorkerAuth.getLogger().custom("getUserFromUuid", "SQL request return: " + jsonObject + "\n");
                return jsonObject.toString();
            }
            response.status(200);
            JsonObject object = new JsonObject();
            object.addProperty("exists", Boolean.TRUE);
            object.addProperty("automine", account.hasAutomine());
            object.addProperty("forage", account.hasForaging());

            WorkerAuth.getLogger().custom("getUserFromUuid", "SQL request positive!");
            WorkerAuth.getLogger().custom("getUserFromUuid", "SQL request report: [USER] \"" + uuid + "\" exist.");
            WorkerAuth.getLogger().custom("getUserFromUuid", "SQL request return: " + object + "\n");
            return object.toString();
        });

        Spark.post("/Unf6gWbunD2xDFyr", (request, response) -> { // getToken with uuid
            WorkerAuth.getLogger().custom("getTokenFromUuid", "SQL request detected!");
            String uuid = request.queryParams("uuid");

            String token = Database.getTokenFromUuid(uuid);

            response.type("application/json");
            response.status(200);
            JsonObject object = new JsonObject();
            if (token == null) {
                object.addProperty("exists", Boolean.FALSE);

                WorkerAuth.getLogger().custom("getTokenFromUuid", "SQL request negative!");
                WorkerAuth.getLogger().custom("getTokenFromUuid", "SQL request report: [TOKEN] doesn't exist for [USER] \"" + uuid + "\".");
            } else {
                object.addProperty("exists", Boolean.TRUE);
                object.addProperty("token", token);

                WorkerAuth.getLogger().custom("getTokenFromUuid", "SQL request positive!");
                WorkerAuth.getLogger().custom("getTokenFromUuid", "SQL request report: [TOKEN] \"" + token + "\" exist for [USER] \"" + uuid + "\".");
            }
            WorkerAuth.getLogger().custom("getTokenFromUuid", "SQL request return: " + object + "\n");
            return object.toString();
        });

        Spark.post("/lU4kAftcDb8RJGzI", (request, response) -> { // addUser
            WorkerAuth.getLogger().custom("addUser", "SQL request detected!");

            String uuid = request.queryParams("uuid");

            String token = request.headers("token");
            if (token == null || !token.equals("o5ucShPhjRuJoPP78sRIPJBAC6HTa19GN6OPb")) {
                response.status(404);
                response.type("application/html");

                WorkerAuth.getLogger().custom("addUser", "SQL request denied! Possibly external request?");
                WorkerAuth.getLogger().custom("addUser", "Detected credentials: [UUID] \"" + uuid + "\" [TOKEN] \"" + token + "\".");
                WorkerAuth.getLogger().custom("addUser", "SQL request return: " + "<html><body><h2>404 Not found</h2></body></html>\n");
                return "<html><body><h2>404 Not found</h2></body></html>";
            }

            String discordId = request.queryParams("discordId");
            boolean automine = Boolean.parseBoolean(request.queryParams("automine"));
            boolean foraging = Boolean.parseBoolean(request.queryParams("foraging"));

            Account account = Database.getAccountFromUuid(uuid);
            if(account != null) {
                response.status(500);
                response.type("application/html");

                WorkerAuth.getLogger().custom("addUser", "SQL request negative!");
                WorkerAuth.getLogger().custom("addUser", "SQL request report: [Account] \"" + account.getUuid() + "\" already exist in SQLite.");
                WorkerAuth.getLogger().custom("addUser", "SQL request return: " + "<html><body><h2>500 Internal Server Error</h2></body></html>\n");
                return "<html><body><h2>500 Internal Server Error</h2></body></html>";
            }

            account = new Account();
            account.setUsername(AccountHelper.getAccountUsername(uuid));
            account.setUuid(uuid);
            account.setDiscordId(discordId);
            account.setAutomine(automine);
            account.setForaging(foraging);
            account.setToken(AccountHelper.generateAccountToken());

            if (Database.addAccount(account)) {
                response.status(200);
                JsonObject object = new JsonObject();
                object.addProperty("token", account.getToken());

                WorkerAuth.getLogger().custom("addUser", "SQL request positive!");
                WorkerAuth.getLogger().custom("addUser", "SQL request report: [Account] \"" + account.getUuid() + "\" was added to SQLite." + AccountHelper.getAccountData(account));
                WorkerAuth.getLogger().custom("addUser", "SQL request return: " + object + "\n");
                return object.toString();
            }

            response.status(500);
            response.type("application/html");
            WorkerAuth.getLogger().custom("addUser", "SQL request negative!");
            WorkerAuth.getLogger().custom("addUser", "SQL request report: [Account] \"" + account.getUuid() + "\" was not added to database.");
            WorkerAuth.getLogger().custom("addUser", "SQL request return: " + "<html><body><h2>500 Internal Server Error</h2></body></html>\n");
            return "<html><body><h2>500 Internal Server Error</h2></body></html>";
        });
    }
}