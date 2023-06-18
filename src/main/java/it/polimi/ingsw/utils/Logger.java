package it.polimi.ingsw.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy || HH:mm:ss");

    public static void login(String message) {
        String timestamp = LocalDateTime.now().format(dataFormat);
        String logMessage = String.format("[%s] %s", timestamp, message);
        System.out.println(logMessage + " has connected");
    }

    public static void logout() {
        String timestamp = LocalDateTime.now().format(dataFormat);
        String logMessage = String.format("[%s] %s", timestamp, "an unknown player has disconnected");
        System.out.println(logMessage);
    }

    public static void logout(String message) {
        String timestamp = LocalDateTime.now().format(dataFormat);
        String logMessage = String.format("[%s] %s", timestamp, message);
        System.out.println(logMessage + " has disconnected");
    }

    public static void serverMessage(String message) {
        String timestamp = LocalDateTime.now().format(dataFormat);
        String logMessage = String.format("[%s] %s", timestamp, message);
        System.out.println(logMessage);
    }

    public static void serverError(String message) {
        String timestamp = LocalDateTime.now().format(dataFormat);
        String logMessage = String.format("[%s] %s", timestamp, message);
        System.err.println(logMessage);
    }

    public static void clientError(String message) {
        String timestamp = LocalDateTime.now().format(dataFormat);
        String logMessage = String.format("[%s] %s", timestamp, message);
        System.err.println(logMessage);
    }
}

