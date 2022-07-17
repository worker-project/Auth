package com.workerai.authentication.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.workerai.authentication.database.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(response.toString());

            String username = String.valueOf(obj.get("name"));
            System.out.println(username);
            return username.replace("\"", "");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
