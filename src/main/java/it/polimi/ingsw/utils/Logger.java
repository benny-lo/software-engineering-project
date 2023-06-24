package it.polimi.ingsw.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Logger {
    private static final DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
    private static final String RESET = "\u001B[0m";
    private static final String ITALIC = "\u001B[3m";
    private static boolean testMode = false;

    public static void setTestMode(boolean mode) {
        testMode = mode;
    }

    public static void login(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.out.println(logMessage + " has connected");
        }
    }

    public static void logout() {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, "an unknown player has disconnected");
            System.out.println(logMessage);
        }
    }

    public static void logout(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.out.println(logMessage + " has disconnected");
        }
    }

    public static void serverMessage(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.out.println(logMessage);
        }
    }

    public static void serverError(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.err.println(logMessage);
        }
    }

    public static void clientError(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.err.println(logMessage);
        }
    }

    public static void createGame(int numberPlayers, int numberCommonGoalCards, int id, String nickname) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, nickname);
            System.out.println(logMessage + " has created a game " + ITALIC + "[GameId: " + id + ", Number of Players: " + numberPlayers + ", Number of Common Goal Cards: " + numberCommonGoalCards + "]" + RESET);
        }
    }

    public static void selectGame(int id, String nickname) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, nickname);
            System.out.println(logMessage + " has selected a game " + ITALIC + "[GameId: " + id + "]" + RESET);
        }
    }

    public static void selectItems(int numberItems, String nickname, int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, nickname);
            if (numberItems == 1)
                System.out.println(logMessage + " has selected " + numberItems + " item " + ITALIC + "[GameId: " + id + "]" + RESET);
            else
                System.out.println(logMessage + " has selected " + numberItems + " items " + ITALIC + "[GameId: " + id + "]" + RESET);
        }
    }

    public static void selectColumn(int column, List<Integer> order, String nickname, int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, nickname);
            System.out.println(logMessage + " has selected the column number " + column + " and this order " + order + ITALIC + " [GameId: " + id + "]" + RESET);
        }
    }

    public static void sendMessage(String sender, String receiver, int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, sender);
            System.out.println(logMessage + " has sent a message to " + receiver + ITALIC + " [GameId: " + id + "]" + RESET);
        }
    }

    public static void startGame(int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s]", timestamp);
            System.out.println(logMessage + " game " + id + " has started");
        }
    }

    public static void endGame(int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s]", timestamp);
            System.out.println(logMessage + " game " + id + " has ended");
        }
    }
}