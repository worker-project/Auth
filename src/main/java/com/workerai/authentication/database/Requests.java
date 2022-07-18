package com.workerai.authentication.database;

import com.google.gson.JsonObject;
import com.workerai.authentication.logger.ILogger;
import com.workerai.authentication.logger.Logger;
import com.workerai.authentication.utils.Helper;
import spark.Spark;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Requests {
    private static ILogger LOGGER;

    public static void main(String[] args) throws IOException {
        Database.initDatabase();
        Spark.port(2929);

        final Path logsFolder = Paths.get(".", "logs.log");
        LOGGER = new Logger("[WAuth]", logsFolder);
        if (!logsFolder.toFile().exists()) {
            if (!logsFolder.toFile().createNewFile()) {
                LOGGER.err("Unable to create logs folder");
            }
        }

        Spark.post("/jWuR0gHff54WvVzL", (request, response) -> { //getUser
            LOGGER.custom("getUser", "SQL request detected!");
            String uuid = request.queryParams("uuid");

            Account account = Database.getAccount(uuid);
            response.type("application/json");
            if (account == null) {
                response.status(200);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("exists", Boolean.FALSE);
                LOGGER.custom("getUser", "SQL request negative!");
                LOGGER.custom("getUser", "SQL request report: [USER] " + uuid + " doesn't exist.\n");
                return jsonObject.toString();
            }
            response.status(200);
            JsonObject object = new JsonObject();
            object.addProperty("exists", Boolean.TRUE);
            object.addProperty("automine", account.hasAutomine());
            object.addProperty("forage", account.hasForaging());
            LOGGER.custom("getUser", "SQL request positive!");
            LOGGER.custom("getUser", "SQL request report: [USER] " + uuid + " exist.\n");
            return object.toString();
        });

        Spark.post("/Unf6gWbunD2xDFyr", (request, response) -> { //getToken
            LOGGER.custom("getToken", "SQL request detected!");
            String uuid = request.queryParams("uuid");

            String token = Database.getAccountToken(uuid);

            response.type("application/json");
            response.status(200);
            JsonObject object = new JsonObject();
            if (token == null) {
                object.addProperty("exists", Boolean.FALSE);
                LOGGER.custom("getToken", "SQL request negative!");
                LOGGER.custom("getToken", "SQL request report: [TOKEN] doesn't exist for [USER] " + uuid + ".\n");
            } else {
                object.addProperty("exists", Boolean.TRUE);
                object.addProperty("token", token);
                LOGGER.custom("getToken", "SQL request positive!");
                LOGGER.custom("getToken", "SQL request report: [TOKEN] " + token + " exist for [USER] " + uuid + ".\n");
            }
            return object.toString();
        });

        Spark.post("/lU4kAftcDb8RJGzI", (request, response) -> { //addUser
            LOGGER.custom("addUser", "SQL request detected!");

            String uuid = request.queryParams("uuid");

            String token = request.headers("token");
            if (token == null || !token.equals("o5ucShPhjRuJoPP78sRIPJBAC6HTa19GN6OPb")) {
                response.status(404);
                response.type("application/html");
                LOGGER.custom("addUser", "SQL request denied! Possibly external request?");
                LOGGER.custom("addUser", "Detected credentials: [UUID] " + uuid + " [TOKEN] " + token + "\n");
                return "<html><body><h2>404 Not found</h2></body></html>";
            }

            String discordId = request.queryParams("discordId");
            boolean automine = Boolean.parseBoolean(request.queryParams("automine"));
            boolean foraging = Boolean.parseBoolean(request.queryParams("foraging"));

            Account account = Database.getAccount(uuid);

            if (account == null) {
                account = new Account();
                account.setUsername(Helper.getAccountUsername(uuid));
                account.setUuid(uuid);
                account.setDiscordId(discordId);
                account.setAutomine(automine);
                account.setForaging(foraging);
                account.setToken(Helper.generateAccountToken());

                if (Database.addAccount(account)) {
                    response.status(200);
                    JsonObject object = new JsonObject();
                    object.addProperty("token", account.getToken());
                    LOGGER.custom("addUser", "SQL request positive!");
                    LOGGER.custom("addUser", "SQL request report: Account was added to database." + account.displayAccountData());
                    return object.toString();
                }
            }

            response.status(500);
            response.type("application/html");
            LOGGER.custom("addUser", "SQL request negative!");
            LOGGER.custom("addUser", "SQL request report: Account was not added to database.\n");
            return "<html><body><h2>500 Internal Server Error</h2></body></html>";
        });
    }
}