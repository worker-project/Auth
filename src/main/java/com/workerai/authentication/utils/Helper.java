package com.workerai.authentication.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public abstract class Helper {
    public static String generateAccountToken() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            token.append((char) ((int) Math.floor(Math.random() * 26.0D) + 97));
        }
        return token.toString();
    }

    public static String getAccountUsername(String uuid) {
        try {
            URL url = new URL("https://api.minetools.eu/uuid/" + uuid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                StringBuilder inline = new StringBuilder();
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline.append(sc.nextLine());
                }
                sc.close();

                JsonParser parse = new JsonParser();
                JsonObject data = (JsonObject) parse.parse(inline.toString());
                String username = String.valueOf(data.get("name"));
                return username.replace("\"", "");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
