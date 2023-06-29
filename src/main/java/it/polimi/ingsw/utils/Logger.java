package it.polimi.ingsw.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Class representing the Logger.
 */
public class Logger {
    private static final DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
    private static final String RESET = "\u001B[0m";
    private static final String ITALIC = "\u001B[3m";
    private static boolean testMode = false;

    /**
     * Sets the test mode.
     * @param mode If it is true then the {@code Logger} won't print anything, otherwise it will work normally.
     */
    public static void setTestMode(boolean mode) {
        testMode = mode;
    }

    /**
     * Prints that a player has connected to the server.
     * It synchronizes on {@code System.out}.
     * @param message The nickname of the player.
     */
    public static void login(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.out.println(logMessage + " has connected");
        }
    }

    /**
     * Prints that an unknown player has disconnected from the server.
     * It synchronizes on {@code System.out}.
     */
    public static void logout() {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, "an unknown player has disconnected");
            System.out.println(logMessage);
        }
    }

    /**
     * Prints that a player has disconnected from the server.
     * It synchronizes on {@code System.out}.
     * @param message The nickname of the player.
     */
    public static void logout(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.out.println(logMessage + " has disconnected");
        }
    }

    /**
     * Prints a message received by the server.
     * It synchronizes on {@code System.out}.
     * @param message The message that will be printed.
     */
    public static void serverMessage(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.out.println(logMessage);
        }
    }

    /**
     * Prints an error received by the server.
     * It synchronizes on {@code System.out}.
     * @param message The error that will be printed.
     */
    public static void serverError(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.err.println(logMessage);
        }
    }

    /**
     * Prints an error received by the client.
     * It synchronizes on {@code System.out}.
     * @param message The error that will be printed.
     */
    public static void clientError(String message) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, message);
            System.err.println(logMessage);
        }
    }

    /**
     * Prints that a game has been created by a player, and prints its attributes.
     * It synchronizes on {@code System.out}.
     * @param numberPlayers The number of players that have to connect to start the game.
     * @param numberCommonGoalCards The number of CommonGoalCards in the game.
     * @param id The ID of the game created.
     * @param nickname The nickname of the player that has created the game.
     */
    public static void createGame(int numberPlayers, int numberCommonGoalCards, int id, String nickname) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, nickname);
            System.out.println(logMessage + " has created a game " + ITALIC + "[GameId: " + id + ", Number of Players: " + numberPlayers + ", Number of Common Goal Cards: " + numberCommonGoalCards + "]" + RESET);
        }
    }

    /**
     * Prints that a game has been selected by a player, and prints its ID.
     * It synchronizes on {@code System.out}.
     * @param id The ID of the game selected.
     * @param nickname The nickname of the player that has selected the game.
     */
    public static void selectGame(int id, String nickname) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, nickname);
            System.out.println(logMessage + " has selected a game " + ITALIC + "[GameId: " + id + "]" + RESET);
        }
    }

    /**
     * Prints the items selected by a player, and prints the game ID.
     * It synchronizes on {@code System.out}.
     * @param numberItems The number of items that have been selected.
     * @param nickname The nickname of the player that has selected the items.
     * @param id The ID of the game.
     */
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

    /**
     * Prints the colum and the order selected by the player, and prints the game ID.
     * It synchronizes on {@code System.out}.
     * @param column The column selected.
     * @param order The order selected.
     * @param nickname The nickname of the player.
     * @param id The ID of the game.
     */
    public static void selectColumn(int column, List<Integer> order, String nickname, int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, nickname);
            System.out.println(logMessage + " has selected the column number " + column + " and this order " + order + ITALIC + " [GameId: " + id + "]" + RESET);
        }
    }

    /**
     * Prints that a message has been sent from a player, and prints the game ID.
     * It synchronizes on {@code System.out}.
     * @param sender The nickname of the sender.
     * @param receiver The nickname of the receiver.
     * @param id The ID of the game.
     */
    public static void sendMessage(String sender, String receiver, int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s] %s", timestamp, sender);
            System.out.println(logMessage + " has sent a message to " + receiver + ITALIC + " [GameId: " + id + "]" + RESET);
        }
    }

    /**
     * Prints that a game has started, and prints the game ID.
     * It synchronizes on {@code System.out}.
     * @param id The ID of the game.
     */
    public static void startGame(int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s]", timestamp);
            System.out.println(logMessage + " game " + id + " has started");
        }
    }

    /**
     * Prints that a game has ended, and prints the game ID.
     * It synchronizes on {@code System.out}.
     * @param id The ID of the game.
     */
    public static void endGame(int id) {
        synchronized (System.out) {
            if (testMode) return;
            String timestamp = LocalDateTime.now().format(dataFormat);
            String logMessage = String.format("[%s]", timestamp);
            System.out.println(logMessage + " game " + id + " has ended");
        }
    }
}