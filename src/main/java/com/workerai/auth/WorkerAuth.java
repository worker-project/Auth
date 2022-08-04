package com.workerai.auth;

import com.workerai.auth.database.Requests;
import com.workerai.auth.logger.ILogger;
import com.workerai.auth.logger.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkerAuth {
    private static ILogger LOGGER;

    public static void main(String[] args) throws IOException {
        final Path logsFolder = Paths.get(".", "logs.log");
        if (!logsFolder.toFile().exists()) {
            if (!logsFolder.toFile().createNewFile()) {
                System.out.println("Unable to create logs file");
                System.exit(-1);
            }
        }

        LOGGER = new Logger("[WAuth]", logsFolder);

        Requests.initRequests();
    }

    public static ILogger getLogger() {
        return LOGGER;
    }
}
