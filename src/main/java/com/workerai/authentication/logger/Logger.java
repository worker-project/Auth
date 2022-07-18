package com.workerai.authentication.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger implements ILogger {
    private final String prefix;
    private final Path logPath;
    private PrintWriter writer;

    public Logger(String prefix, Path logPath, boolean append) {
        this.prefix = prefix.endsWith(" ") ? prefix : prefix + " ";
        this.logPath = logPath;

        if (this.logPath != null) {
            try {
                if (Files.notExists(this.logPath)) {
                    Files.createDirectories(this.logPath.getParent());
                    Files.createFile(this.logPath);
                }
                if (append)
                    this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(this.logPath, StandardOpenOption.APPEND), StandardCharsets.UTF_8)));
                else
                    this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(this.logPath), StandardCharsets.UTF_8)));
                Runtime.getRuntime().addShutdownHook(new Thread(this.writer::close));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Logger(String prefix, Path logFile) {
        this(prefix, logFile, false);
    }

    public void message(boolean err, String toWrite) {
        final String date = String.format("[%s] ", new SimpleDateFormat("hh:mm:ss").format(new Date()));
        final String msg = date + this.prefix + (err ? "[ERROR]: " : "[INFO]: ") + toWrite;
        if (err) System.out.println(EnumLogColor.RED + msg + EnumLogColor.RESET);
        else System.out.println(msg);
        this.writeToTheLogFile(msg);
    }

    @Override
    public void err(String message) {
        this.message(true, message);
    }

    @Override
    public void custom(String requestName, String logMessage) {
        requestName += " ";
        logMessage += " ";

        System.out.println();

        final String date = String.format("[%s] ", new SimpleDateFormat("hh:mm:ss").format(new Date()));
        System.out.print(EnumLogColor.PURPLE + date + EnumLogColor.RESET);

        final String prefix = this.prefix;
        System.out.print(EnumLogColor.RED + prefix + EnumLogColor.RESET);

        final String request = "[Request]: ";
        System.out.print(EnumLogColor.YELLOW + request + EnumLogColor.RESET + requestName);

        final String log = "[Log]: ";
        System.out.print(EnumLogColor.YELLOW + log + EnumLogColor.RESET + logMessage);

        final String msg = date + prefix + request + requestName + log + logMessage;
        this.writeToTheLogFile(msg);
    }

    @Override
    public void writeToTheLogFile(String toLog) {
        if (this.logPath != null) {
            try {
                if (Files.notExists(this.logPath)) {
                    Files.createDirectories(this.logPath.getParent());
                    Files.createFile(this.logPath);
                }

                if (this.writer != null) {
                    this.writer.println(toLog);
                    this.writer.flush();
                }
            } catch (IOException e) {
                this.printStackTrace(e);
            }
        }
    }

    @Override
    public void printStackTrace(Throwable cause) {
        this.printStackTrace("An error occurred : ", cause);
    }

    @Override
    public void printStackTrace(String errorName, Throwable cause) {
        this.err(errorName + cause.toString());
        for (StackTraceElement trace : cause.getStackTrace()) {
            final String toPrint = "\tat " + trace.toString();
            this.writeToTheLogFile(toPrint);
            System.out.println(EnumLogColor.RED + toPrint + EnumLogColor.RESET);
        }
    }
}
