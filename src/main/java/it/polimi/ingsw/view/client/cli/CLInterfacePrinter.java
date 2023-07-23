package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.utils.view.CommonGoalCardDescription;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.message.server.ChatUpdate;
import it.polimi.ingsw.utils.message.server.GameInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private static final ClearablePrinter printer = new ClearablePrinter();

    /**
     * Prints the current player.
     * @param nickname The nickname of the player owning the client.
     * @param currentPlayer The nickname of the current player.
     */
     public static void printCurrentPlayer(String nickname, String currentPlayer){
        if (currentPlayer == null) return;
        if (currentPlayer.equals(nickname)) {
            printer.print(ANSI_RED + "You are the current player" + ANSI_RESET + "\n\n");
        } else {
            printer.print(ANSI_RED + "The current player is " + currentPlayer + ANSI_RESET + "\n\n");
        }
    }

    /**
     * Prints the welcome message.
     */
    public static void printWelcomeMessage(){
        printer.print("""
                Welcome to MyShelfie!
                Digit '/help' for all the commands!
                """);
    }

    /**
     * Prints the help menu.
     */
    public static void printHelp(){
        printer.print("""
                Commands list:\s
                \t /login [nickname]\s
                \t /create_game [number_of_players] [number_of_common_goal_cards]
                \t /select_game [id]\s
                \t /living_room [positions]\s
                \t /bookshelf [column] [permutation]\s
                \t /enter_chat\s
                \t /dm [other_player_name]
                \t /exit_chat\s
                \t /exit
                """);
    }

    /**
     * Prints the wrong command error message.
     */
    public static void printWrongCommand() {
        printer.print("This command does not exists! Try to digit '/help' for other commands.\n");
    }

    /**
     * Prints the incorrect command error message.
     */
    public static void printIncorrectCommand(){
        printer.print("This command is misspelled! Try to digit '/help' for other commands.\n");
    }

    /**
     * Prints a warning in case the player's status doesn't match the status required by the action that needs
     * to be completed.
     */
    public static void printWrongStatus(){
        printer.print("You cannot do this action now!\nTry to digit '/help' for other commands.\n");
    }

    /**
     * Prints the login failed error message.
     */
    public static void printLoginFailed(){
        printer.print("Login failed. Try again!\n");
    }

    /**
     * Warns the player of the absence of available games.
     */
    public static void printNoAvailableGames(){
        printer.print("There are no available games.\nCreate a new game with the command '/create_game [number_of_players] [number_of_common_goal_cards]'.\n");
    }

    /**
     * Warns the player of having inserted an incorrect nickname.
     */
    public static void printIncorrectNickname(){
        printer.print("""
                This nickname is incorrect! Retry to login.
                Your nickname has to be at least 1 character and less than 30.
                It can only contains alphanumeric characters and underscores.
                """);
    }

    /**
     * Warns the player of having selected an invalid tile.
     */
    public static  void printInvalidSelection(){
        printer.print("Invalid tile selection! Try again.\n");
    }

    /**
     * Prints the denied action error message.
     */
    public static void printDeniedAction(){
        printer.print("Action denied. Try again!\n");
    }

    /**
     * Informs the player that they have entered the chat.
     */
    public static void printInChat(){
        printer.print("You've now entered in the chat!\n");
    }

    /**
     * Informs the player that they have not yet entered the chat.
     */
    public static void printNotInChat(){
        printer.print("You've not entered in the chat yet!\nTry with the command '/enter_chat'.\n");
    }

    /**
     * Informs the player that they have exited the chat.
     */
    public static void printExitChat(){
        printer.print("You've now left the chat!\n");
    }

    /**
     * Clears the screen.
     */
    public static void clearScreen() {
        printer.clear();
    }

    /**
     * Prints out the living room.
     * @param livingRoom The living room that needs to be printed.
     */
    public static void printLivingRoom(Item[][] livingRoom) {
        if (livingRoom == null) return;

        printer.print("LivingRoom: \n");
        printer.print("  0 1 2 3 4 5 6 7 8\n");
        int i = 0;
        for (Item[] items : livingRoom) {
            printer.print(i + " ");
            i++;
            for (Item item : items) {
                printItem(item);
            }
            printer.print("\n");
        }
        printer.print("\n");
    }

    /**
     * Prints an item of the living room or of the bookshelves.
     * @param item The item that needs to be printed.
     */
    public static void printItem(Item item) {
        if (item == null) {
            printer.print(ANSI_RED + "X " + ANSI_RESET);
            return;
        }

        switch (item) {
            case CAT -> printer.print(ANSI_GREEN + "C " + ANSI_RESET);
            case BOOK -> printer.print(ANSI_WHITE + "B " + ANSI_RESET);
            case GAME -> printer.print(ANSI_YELLOW + "G " + ANSI_RESET);
            case FRAME -> printer.print(ANSI_BLUE + "F " + ANSI_RESET);
            case CUP -> printer.print(ANSI_CYAN + "T " + ANSI_RESET);
            case PLANT -> printer.print(ANSI_PURPLE + "P " + ANSI_RESET);
            case LOCKED -> printer.print(ANSI_RED + "X " + ANSI_RESET);
        }
    }

    /**
     * Prints the bookshelves of the players.
     * @param bookshelves The bookshelves that need to be printed.
     */
    public static void printBookshelves(Map<String, Item[][]> bookshelves, Set<String> disconnectedPlayers) {
        if (bookshelves == null) return;
        printer.print("These are the respective Bookshelves of: \n");
        for (String player : bookshelves.keySet()) {
            if (disconnectedPlayers.contains(player)) continue;
            printer.print(player);
            printBookshelfOrPersonalGoalCard(bookshelves.get(player));
        }
        printer.print("\n");
    }

    /**
     * Prints a bookshelf or a singular personal goal card.
     * @param array The position of the items of the personal goal card or of the bookshelf.
     */
    public static void printBookshelfOrPersonalGoalCard(Item[][] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            printer.print(i + " ");
            for (int j = 0; j < array[i].length; j++) {
                printItem(array[i][j]);
            }
            printer.print("\n");
        }
        printer.print("  0 1 2 3 4\n\n");
    }

    /**
     * Prints the personal goal card of the player.
     * @param personalGoalCard The personal goal card that needs to be printed.
     */
    public static void printPersonalGoalCard(Item[][] personalGoalCard) {
        if (personalGoalCard == null) return;
        printer.print("Your personal goal card is: \n");
        printBookshelfOrPersonalGoalCard(personalGoalCard);
    }

    /**
     * Prints the common goal cards of the game.
     * @param commonGoalCards The common goal cards that need to be printed.
     */
    public static void printCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        if (commonGoalCards == null) return;
        String description;
        printer.print("Your common goal cards are: \n");
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            description = CommonGoalCardDescription.getDescription(card.getKey());
            printer.print("Id: " + card.getKey() + ", Top token value: " + card.getValue() + description + "\n");
        }
    }

    /**
     * Prints the chosen items.
     * @param itemsChosen The item chosen by the player.
     * @param currentPlayer The player that has chosen the items.
     */
    public static void printItemsChosen(List<Item> itemsChosen, String currentPlayer) {
        if (currentPlayer == null) return;
        if (itemsChosen == null) return;
        System.out.print(currentPlayer + " chose the items: ");
        for (Item item : itemsChosen) {
            System.out.print(item + " ");
        }
        printer.print("\n");
    }

    /**
     * Prints the ending token.
     * @param endingToken The endingToken.
     */
    public static void printEndingToken(String endingToken) {
        if (endingToken != null) {
            printer.print(endingToken + " has the ending token\n");
        }
    }

    /**
     * Prints the scores of the players.
     * @param scores The scores of the players.
     */
    public static void printScores(Map<String, Integer> scores) {
        if (scores == null) return;
        printer.print("Ranking: \n");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            printer.print(e.getKey() + ": " + e.getValue() + "\n");
        }
    }

    /**
     * Prints the chat messages.
     * @param chat The chat that needs to be printed.
     */
    public static void printChat(List<ChatUpdate> chat) {
        if (chat.size() == 0) {
            printer.print("No message in chat yet\n");
            return;
        }

        for (ChatUpdate update : chat) {
            printer.print(update.getSender() + " wrote to " + update.getReceiver() + " : " + update.getText() + "\n");
        }
    }

    /**
     * Prints the end game messages.
     * @param nickname The nickname of the winner.
     * @param winner The winner.
     * @param scores The scores of the players.
     */
    public static void printEndGame(String nickname, String winner, Map<String, Integer> scores) {
        if (winner == null){
            printer.print("The game has terminated, as somebody disconnected.\n");
        } else if (scores == null) {
            return;
        } else if (winner.equals(nickname)) {
            printer.print("You are the winner\n");
            printScores(scores);
        } else {
            printer.print("The winner is " + winner + "\n");
            printScores(scores);
        }

        printer.print("You can now close the game.\n");
    }

    /**
     * Prints the lost connection error message.
     */
    public static void printLostConnection() {
        printer.print("Lost connection to server.\n");
    }

    /**
     * Prints the failed personal goal card configuration message.
     */
    public static void printPersonalGoalCardConfigurationFailed(){
        System.err.println("""
                    Configuration file for personalGoalCard not found.
                    The configuration file should be in configuration/personalGoalCard
                    with name personal_goal_pattern_{id}""");
    }

    /**
     * Prints the player's nickname that has just connected.
     * @param nickname The nickname of the player.
     */
    public static void printPlayerJustConnected(String nickname){
        printer.print(nickname + " just connected\n");
    }

    /**
     * Prints the player's nickname that has disconnected.
     * @param nickname The nickname of the player.
     */
    public static void printPlayerJustDisconnected(String nickname){
        printer.print(nickname + " just disconnected\n");
    }

    /**
     * Prints the list of available games.
     * @param games The list of games.
     */
    public static void printGamesList(List<GameInfo> games){
        printer.print("Select a game from the list:\n");
        for (GameInfo info : games) {
            printer.print(info.toString());
        }
    }

    /**
     * Prints the number of connected players and how many players have to connect.
     * @param missing Number of players missing.
     * @param connectedPlayers Number of connected players.
     */
    public static void printNumberMissingPlayers(int missing, Collection<String> connectedPlayers){
        if (connectedPlayers == null) return;

        printer.print("Connected players: \n");
        for (String player : connectedPlayers) {
            printer.print(player + "\n");
        }

        if (missing == 1)
            printer.print("Waiting for " + missing + " player ...\n");
        else
            printer.print("Waiting for " + missing + " players ...\n");
    }

    /**
     * Prints the start game message.
     */
    public static void printStartGame(){
        printer.print("Game starts!\n");
    }

    /**
     * Prints the exit message.
     */
    public static void printExit(){
        printer.print("Digit '/exit' to close the launcher.\n");
    }

    public static void printReconnection(String myNickname, String reconnectedNickname) {
        String re = (!reconnectedNickname.equals(myNickname)) ? reconnectedNickname : "you";

        printer.print(ANSI_RED + re + " reconnected!!!" + ANSI_RESET + "\n");
    }

    public static void printDisconnected(String disconnectedPlayer) {
        printer.print(ANSI_RED + disconnectedPlayer + " disconnected!!!" + ANSI_RESET + "\n");
    }
}
