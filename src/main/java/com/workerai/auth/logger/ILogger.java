package com.workerai.auth.logger;

public interface ILogger {
    void custom(String custom, String message);

    void printStackTrace(Throwable cause);

    default void writeToLogFile(String toLog) {}

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
