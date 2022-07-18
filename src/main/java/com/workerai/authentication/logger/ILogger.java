package com.workerai.authentication.logger;

public interface ILogger {
    void err(String message);

    void custom(String custom, String message);

    void printStackTrace(String errorName, Throwable cause);

    void printStackTrace(Throwable cause);

    default void writeToTheLogFile(String toLog) {}

    enum EnumLogColor {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m");

        private final String color;

        EnumLogColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return this.getColor();
        }
    }
}
