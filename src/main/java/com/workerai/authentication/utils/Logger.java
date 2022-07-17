package com.workerai.authentication.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Logger {
    private final String loggerName;

    public Logger(String loggerName) {
        this.loggerName = loggerName;
    }

    private String colorLog(String logColor, String message) {
        switch (logColor) {
            case "BLUE":
                return "\033[0;34m" + message;
            case "GREEN":
                return "\033[0;32m" + message;
            case "YELLOW":
                return "\033[0;33m" + message;
            case "RED":
                return "\033[0;31m" + message;
            case "PURPLE":
                return "\033[0;35m" + message;
            default:
                return "\033[0m" + message;
        }
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    private static String displayTime(long timeStamp) { return SDF.format(timeStamp / 1000); }

    public void Log(String requestType, String message) {
        System.out.println(
                colorLog("PURPLE", displayTime(System.currentTimeMillis() * 1000) + " ") + colorLog("RED", "[" + loggerName + "]") +
                        colorLog("YELLOW", " Request: ") + colorLog("WHITE", requestType) +
                        colorLog("YELLOW", " Log: ") + colorLog("WHITE", message)
        );
    }
}
