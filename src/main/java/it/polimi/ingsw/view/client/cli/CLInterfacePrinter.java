package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.utils.view.CommonGoalCardDescription;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.message.server.ChatUpdate;
import it.polimi.ingsw.utils.message.server.GameInfo;

import java.io.PrintStream;
import java.util.*;

/**
 * Class representing the CLIPrinter.
 */
class CLInterfacePrinter {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final PrintStream ps = new PrintStream(System.out);

    private CLInterfacePrinter() {}

    /**
     * Prints the current player.
     * @param nickname The nickname of the player owning the client.
     * @param currentPlayer The nickname of the current player.
     */
    static void printCurrentPlayer(String nickname, String currentPlayer) {
        ps.println(ANSI_YELLOW + "Hi " + nickname + "!" + ANSI_RESET + "\n");

        if (currentPlayer == null) {
            ps.println(ANSI_YELLOW + "Waiting for the other players..." + ANSI_RESET + "\n");
            return;
        }

        if (currentPlayer.equals(nickname)) {
            ps.println(ANSI_YELLOW + "You are the current player." + ANSI_RESET + "\n");
        } else {
            ps.println(ANSI_YELLOW + "The current player is " + currentPlayer + ANSI_RESET + "\n");
        }
    }

    /**
     * Prints the welcome message.
     */
    static void printWelcomeMessage() {
        ps.println("""
                Welcome to MyShelfie!
                Digit '/help' for all the commands!""");
    }

    /**
     * Prints the help menu.
     */
    static void printHelp() {
        ps.println("""
                Commands list:\s
                \t /login [nickname]\s
                \t /create_game [number_of_players] [number_of_common_goal_cards]
                \t /select_game [id]\s
                \t /living_room [positions]\s
                \t /bookshelf [column] [permutation]\s
                \t /enter_chat\s
                \t /dm [other_player_name]
                \t /exit_chat\s
                \t /exit""");
    }

    /**
     * Prints the wrong command error message.
     */
    static void printWrongCommand() {
        ps.println("This command does not exists! Try to digit '/help' for other commands.");
    }

    /**
     * Prints the incorrect command error message.
     */
    static void printIncorrectCommand() {
        ps.println("This command is misspelled! Try to digit '/help' for other commands.");
    }

    /**
     * Prints a warning in case the player's status doesn't match the status required by the action that needs
     * to be completed.
     */
    static void printWrongStatus() {
        ps.println("You cannot do this action now!\nTry to digit '/help' for other commands.");
    }

    /**
     * Prints the login failed error message.
     */
    static void printLoginFailed(){
        ps.println("Login failed. Try again!");
    }

    /**
     * Warns the player of the absence of available games.
     */
    static void printNoAvailableGames() {
        ps.println("There are no available games.\nCreate a new game with the command '/create_game [number_of_players] [number_of_common_goal_cards]'.");
    }

    /**
     * Warns the player of having inserted an incorrect nickname.
     */
    static void printIncorrectNickname() {
        ps.println("""
                This nickname is incorrect! Retry to login.
                Your nickname has to be at least 1 character and less than 30.
                It can only contains alphanumeric characters and underscores.""");
    }

    /**
     * Warns the player of having selected an invalid tile.
     */
    static  void printInvalidSelection(){
        ps.println("Invalid tile selection! Try again.");
    }

    /**
     * Prints the denied action error message.
     */
    static void printDeniedAction(){
        ps.println("Action denied. Try again!");
    }

    /**
     * Informs the player that they have entered the chat.
     */
    static void printInChat(){
        ps.println("You've now entered in the chat!");
    }

    /**
     * Informs the player that they have not yet entered the chat.
     */
    static void printNotInChat(){
        ps.println("You've not entered in the chat yet!\nTry with the command '/enter_chat'.");
    }

    /**
     * Informs the player that they have exited the chat.
     */
    static void printExitChat(){
        ps.println("You've now left the chat!");
    }

    /**
     * Clears the screen.
     */
    static void clearScreen() {
        ps.print("\033c");
    }

    /**
     * Prints out the living room.
     * @param livingRoom The living room that needs to be printed.
     */
    static void printLivingRoom(Item[][] livingRoom) {
        if (livingRoom == null) return;

        ps.println("LivingRoom: ");
        ps.println("  0 1 2 3 4 5 6 7 8");
        int i = 0;
        for (Item[] items : livingRoom) {
            ps.print(i + " ");
            i++;
            for (Item item : items) {
                printItem(item);
            }
            ps.println();
        }
        ps.println();
    }

    /**
     * Prints an item of the living room or of the bookshelves.
     * @param item The item that needs to be printed.
     */
    static void printItem(Item item) {
        if (item == null) {
            ps.print(ANSI_RED + "X " + ANSI_RESET);
            return;
        }

        switch (item) {
            case CAT -> ps.print(ANSI_GREEN + "C " + ANSI_RESET);
            case BOOK -> ps.print(ANSI_WHITE + "B " + ANSI_RESET);
            case GAME -> ps.print(ANSI_YELLOW + "G " + ANSI_RESET);
            case FRAME -> ps.print(ANSI_BLUE + "F " + ANSI_RESET);
            case CUP -> ps.print(ANSI_CYAN + "T " + ANSI_RESET);
            case PLANT -> ps.print(ANSI_PURPLE + "P " + ANSI_RESET);
            case LOCKED -> ps.print(ANSI_RED + "X " + ANSI_RESET);
        }
    }

    /**
     * Prints the bookshelves of the players.
     * @param bookshelves The bookshelves that need to be printed.
     */
    static void printBookshelves(Map<String, Item[][]> bookshelves, Set<String> disconnectedPlayers) {
        if (bookshelves == null) return;
        ps.println("These are the respective Bookshelves of: ");
        for (Map.Entry<String, Item[][]> e : bookshelves.entrySet()) {
            if (disconnectedPlayers.contains(e.getKey())) continue;
            ps.println(e.getKey());
            printBookshelfOrPersonalGoalCard(e.getValue());
        }
        ps.println();
    }

    /**
     * Prints a bookshelf or a singular personal goal card.
     * @param array The position of the items of the personal goal card or of the bookshelf.
     */
    static void printBookshelfOrPersonalGoalCard(Item[][] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            ps.print(i + " ");
            for (int j = 0; j < array[i].length; j++) {
                printItem(array[i][j]);
            }
            ps.println();
        }
        ps.println("  0 1 2 3 4");
        ps.println();
    }

    /**
     * Prints the personal goal card of the player.
     * @param personalGoalCard The personal goal card that needs to be printed.
     */
    static void printPersonalGoalCard(Item[][] personalGoalCard) {
        if (personalGoalCard == null) return;
        ps.println("Your personal goal card is: ");
        printBookshelfOrPersonalGoalCard(personalGoalCard);
    }

    /**
     * Prints the common goal cards of the game.
     * @param commonGoalCards The common goal cards that need to be printed.
     */
    static void printCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        if (commonGoalCards == null) return;
        String description;
        ps.println("Your common goal cards are: ");
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            description = CommonGoalCardDescription.getDescription(card.getKey());
            ps.println("Id: " + card.getKey() + ", Top token value: " + card.getValue() + description);
        }
    }

    /**
     * Prints the chosen items.
     * @param itemsChosen The item chosen by the player.
     * @param currentPlayer The player that has chosen the items.
     */
    static void printItemsChosen(List<Item> itemsChosen, String currentPlayer) {
        if (currentPlayer == null) return;
        if (itemsChosen == null) return;
        ps.print(currentPlayer + " chose the items: ");
        for (Item item : itemsChosen) {
            ps.print(item + " ");
        }
        ps.println();
    }

    /**
     * Prints the ending token.
     * @param endingToken The endingToken.
     */
    static void printEndingToken(String endingToken) {
        if (endingToken != null) {
            ps.println(endingToken + " has the ending token");
        }
    }

    /**
     * Prints the scores of the players.
     * @param scores The scores of the players.
     */
    static void printScores(Map<String, Integer> scores) {
        if (scores == null) return;
        ps.println("Ranking:");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            ps.println(e.getKey() + ": " + e.getValue());
        }
        ps.println();
    }

    /**
     * Prints the chat messages.
     * @param chat The chat that needs to be printed.
     */
    static void printChat(List<ChatUpdate> chat) {
        if (chat.isEmpty()) {
            ps.println("No message in chat yet");
            return;
        }

        for (ChatUpdate update : chat) {
            ps.println(update.getSender() + " wrote to " + update.getReceiver() + " : " + update.getText());
        }
    }

    /**
     * Prints the end game messages.
     * @param nickname The nickname of the winner.
     * @param winner The winner.
     * @param scores The scores of the players.
     */
    static void printEndGame(String nickname, String winner, Map<String, Integer> scores) {
        if (winner == null){
            ps.println("The game has terminated, as somebody disconnected.");
        } else if (scores == null) {
            return;
        } else if (winner.equals(nickname)) {
            ps.println("You are the winner");
            printScores(scores);
        } else {
            ps.println("The winner is " + winner);
            printScores(scores);
        }

        ps.println("You can now close the game.");
    }

    /**
     * Prints the lost connection error message.
     */
    static void printLostConnection() {
        ps.println("Lost connection to server.");
    }

    /**
     * Prints the failed personal goal card configuration message.
     */
    static void printPersonalGoalCardConfigurationFailed() {
        ps.println("""
                    Configuration file for personalGoalCard not found.
                    The configuration file should be in configuration/personalGoalCard
                    with name personal_goal_pattern_{id}""");
    }

    /**
     * Prints the player's nickname that has just connected.
     * @param nickname The nickname of the player.
     */
    static void printPlayerJustConnected(String nickname){
        ps.println(nickname + " just connected");
    }

    /**
     * Prints the player's nickname that has disconnected.
     * @param nickname The nickname of the player.
     */
    static void printPlayerJustDisconnected(String nickname){
        ps.println(nickname + " just disconnected");
    }

    /**
     * Prints the list of available games.
     * @param games The list of games.
     */
    static void printGamesList(List<GameInfo> games) {
        ps.println("Create a new game with the command '/create_game [number_of_players] [number_of_common_goal_cards]'.");
        ps.println("Select a game from the list:");
        for (GameInfo info : games) {
            ps.println(info);
        }
    }

    /**
     * Prints the number of connected players and how many players have to connect.
     * @param missing Number of players missing.
     * @param connectedPlayers Number of connected players.
     */
    static void printNumberMissingPlayers(int missing, Collection<String> connectedPlayers) {
        if (connectedPlayers == null) return;

        ps.println("Connected players: ");
        for (String player : connectedPlayers) {
            ps.println(player);
        }

        if (missing == 1)
            ps.println("Waiting for " + missing + " player ...");
        else
            ps.println("Waiting for " + missing + " players ...");
    }

    /**
     * Prints the start game message.
     */
    static void printStartGame(){
        ps.println("Game starts!");
    }

    /**
     * Prints the exit message.
     */
    static void printExit(){
        ps.println("Digit '/exit' to close the launcher.");
    }

    /**
     * Prints the nickname of the player that has reconnected.
     * @param myNickname The client's nickname.
     * @param reconnectedNickname The nickname of the player that has reconnected.
     */
    static void printReconnection(String myNickname, HashSet<String> reconnectedNickname) {
        if (reconnectedNickname.contains(myNickname)) {
            ps.println(ANSI_RED + "You have reconnected!" + ANSI_RESET);
            reconnectedNickname.remove(myNickname);
        }
        if (!reconnectedNickname.isEmpty())
            for (String player : reconnectedNickname) {
                ps.println(ANSI_RED + player + " has reconnected!" + ANSI_RESET);
            }
    }

    /**
     * Prints the nicknames of the players that have disconnected.
     * @param disconnectedPlayers The nicknames of the players that have disconnected.
     */
    static void printDisconnectedPlayers(HashSet<String> disconnectedPlayers) {
        for (String player : disconnectedPlayers){
            ps.println(ANSI_RED + player + " has disconnected!" + ANSI_RESET);
        }
    }

    static void flush() {
        ps.flush();
    }
}
