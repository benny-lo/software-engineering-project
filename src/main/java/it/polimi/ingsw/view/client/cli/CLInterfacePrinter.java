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
 * Class representing the CLIClearablePrinter.
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

    /**
     * Prints the current player.
     * @param nickname The nickname of the player owning the client.
     * @param currentPlayer The nickname of the current player.
     */
     public static void printCurrentPlayer(String nickname, String currentPlayer){
        if (currentPlayer == null) return;
        if (currentPlayer.equals(nickname)) {
            ClearablePrinter.printAndClearNext(ANSI_RED + "You are the current player" + ANSI_RESET + "\n\n");
        } else {
            ClearablePrinter.printAndClearNext(ANSI_RED + "The current player is " + currentPlayer + ANSI_RESET + "\n\n");
        }
    }

    /**
     * Prints the welcome message.
     */
    public static void printWelcomeMessage(){
        ClearablePrinter.printAndNoClearNext("""
                Welcome to MyShelfie!
                Digit '/help' for all the commands!
                """);
    }

    /**
     * Prints the help menu.
     */
    public static void printHelp(){
        ClearablePrinter.printAndClearNext("""
                        Commands list:
                            /login [nickname]
                            /create_game [number_of_players] [number_of_common_goal_cards]
                            /select_game [id]
                            /living_room [positions]
                            /bookshelf [column] [permutation]
                            /enter_chat
                            /dm [other_player_name]
                            /exit_chat
                            /exit
                        """);
    }

    /**
     * Prints the wrong command error message.
     */
    public static void printWrongCommand() {
        ClearablePrinter.printAndClearNext("This command does not exists! Try to digit '/help' for other commands.\n");
    }

    /**
     * Prints the incorrect command error message.
     */
    public static void printIncorrectCommand(){
        ClearablePrinter.printAndClearNext("This command is misspelled! Try to digit '/help' for other commands.\n");
    }

    /**
     * Prints a warning in case the player's status doesn't match the status required by the action that needs
     * to be completed.
     */
    public static void printWrongStatus(){
        ClearablePrinter.printAndClearNext("You cannot do this action now!\nTry to digit '/help' for other commands.\n");
    }

    /**
     * Prints the login failed error message.
     */
    public static void printLoginFailed(){
        ClearablePrinter.printAndClearNext("Login failed. Try again!\n");
    }

    /**
     * Warns the player of the absence of available games.
     */
    public static void printNoAvailableGames(){
        ClearablePrinter.printAndClearNext("There are no available games.\nCreate a new game with the command '/create_game [number_of_players] [number_of_common_goal_cards]'.\n");
    }

    /**
     * Warns the player of having inserted an incorrect nickname.
     */
    public static void printIncorrectNickname(){
        ClearablePrinter.printAndClearNext("""
                This nickname is incorrect! Retry to login.
                Your nickname has to be at least 1 character and less than 30.
                It can only contains alphanumeric characters and underscores.
                """);
    }

    /**
     * Warns the player of having selected an invalid tile.
     */
    public static  void printInvalidSelection(){
        ClearablePrinter.printAndClearNext("Invalid tile selection! Try again.\n");
    }

    /**
     * Prints the denied action error message.
     */
    public static void printDeniedAction(){
        ClearablePrinter.printAndClearNext("Action denied. Try again!\n");
    }

    /**
     * Informs the player that they have entered the chat.
     */
    public static void printInChat(){
        ClearablePrinter.printAndClearNext("You've now entered in the chat!\n");
    }

    /**
     * Informs the player that they have not yet entered the chat.
     */
    public static void printNotInChat(){
        ClearablePrinter.printAndClearNext("You've not entered in the chat yet!\nTry with the command '/enter_chat'.\n");
    }

    /**
     * Informs the player that they have exited the chat.
     */
    public static void printExitChat(){
        ClearablePrinter.printAndClearNext("You've now left the chat!\n");
    }

    /**
     * Clears the screen.
     */
    public static void clearScreen() {
        ClearablePrinter.clear();
    }

    /**
     * Prints out the living room.
     * @param livingRoom The living room that needs to be printed.
     */
    public static void printLivingRoom(Item[][] livingRoom) {
        if (livingRoom == null) return;

        ClearablePrinter.printAndClearNext("LivingRoom: \n");
        ClearablePrinter.printAndClearNext("  0 1 2 3 4 5 6 7 8\n");
        int i = 0;
        for (Item[] items : livingRoom) {
            ClearablePrinter.printAndClearNext(i + " ");
            i++;
            for (Item item : items) {
                printItem(item);
            }
            ClearablePrinter.printAndClearNext("\n");
        }
        ClearablePrinter.printAndClearNext("\n");
    }

    /**
     * Prints an item of the living room or of the bookshelves.
     * @param item The item that needs to be printed.
     */
    public static void printItem(Item item) {
        if (item == null) {
            ClearablePrinter.printAndClearNext(ANSI_RED + "X " + ANSI_RESET);
            return;
        }

        switch (item) {
            case CAT -> ClearablePrinter.printAndClearNext(ANSI_GREEN + "C " + ANSI_RESET);
            case BOOK -> ClearablePrinter.printAndClearNext(ANSI_WHITE + "B " + ANSI_RESET);
            case GAME -> ClearablePrinter.printAndClearNext(ANSI_YELLOW + "G " + ANSI_RESET);
            case FRAME -> ClearablePrinter.printAndClearNext(ANSI_BLUE + "F " + ANSI_RESET);
            case CUP -> ClearablePrinter.printAndClearNext(ANSI_CYAN + "T " + ANSI_RESET);
            case PLANT -> ClearablePrinter.printAndClearNext(ANSI_PURPLE + "P " + ANSI_RESET);
            case LOCKED -> ClearablePrinter.printAndClearNext(ANSI_RED + "X " + ANSI_RESET);
        }
    }

    /**
     * Prints the bookshelves of the players.
     * @param bookshelves The bookshelves that need to be printed.
     */
    public static void printBookshelves(Map<String, Item[][]> bookshelves, Set<String> disconnectedPlayers) {
        if (bookshelves == null) return;
        ClearablePrinter.printAndClearNext("These are the respective Bookshelves of: \n");
        for (String player : bookshelves.keySet()) {
            if (disconnectedPlayers.contains(player)) continue;
            ClearablePrinter.printAndClearNext(player);
            printBookshelfOrPersonalGoalCard(bookshelves.get(player));
        }
        ClearablePrinter.printAndClearNext("\n");
    }

    /**
     * Prints a bookshelf or a singular personal goal card.
     * @param array The position of the items of the personal goal card or of the bookshelf.
     */
    public static void printBookshelfOrPersonalGoalCard(Item[][] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            ClearablePrinter.printAndClearNext(i + " ");
            for (int j = 0; j < array[i].length; j++) {
                printItem(array[i][j]);
            }
            ClearablePrinter.printAndClearNext("\n");
        }
        ClearablePrinter.printAndClearNext("  0 1 2 3 4\n\n");
    }

    /**
     * Prints the personal goal card of the player.
     * @param personalGoalCard The personal goal card that needs to be printed.
     */
    public static void printPersonalGoalCard(Item[][] personalGoalCard) {
        if (personalGoalCard == null) return;
        ClearablePrinter.printAndClearNext("Your personal goal card is: \n");
        printBookshelfOrPersonalGoalCard(personalGoalCard);
    }

    /**
     * Prints the common goal cards of the game.
     * @param commonGoalCards The common goal cards that need to be printed.
     */
    public static void printCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        if (commonGoalCards == null) return;
        String description;
        ClearablePrinter.printAndClearNext("Your common goal cards are: \n");
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            description = CommonGoalCardDescription.getDescription(card.getKey());
            ClearablePrinter.printAndClearNext("Id: " + card.getKey() + ", Top token value: " + card.getValue() + description + "\n");
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
        ClearablePrinter.printAndClearNext("\n");
    }

    /**
     * Prints the ending token.
     * @param endingToken The endingToken.
     */
    public static void printEndingToken(String endingToken) {
        if (endingToken != null) {
            ClearablePrinter.printAndClearNext(endingToken + " has the ending token\n");
        }
    }

    /**
     * Prints the scores of the players.
     * @param scores The scores of the players.
     */
    public static void printScores(Map<String, Integer> scores) {
        if (scores == null) return;
        ClearablePrinter.printAndClearNext("Ranking: \n");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            ClearablePrinter.printAndClearNext(e.getKey() + ": " + e.getValue() + "\n");
        }
    }

    /**
     * Prints the chat messages.
     * @param chat The chat that needs to be printed.
     */
    public static void printChat(List<ChatUpdate> chat) {
        if (chat.size() == 0) {
            ClearablePrinter.printAndClearNext("No message in chat yet\n");
            return;
        }

        for (ChatUpdate update : chat) {
            ClearablePrinter.printAndClearNext(update.getSender() + " wrote to " + update.getReceiver() + " : " + update.getText() + "\n");
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
            ClearablePrinter.printAndClearNext("The game has terminated, as somebody disconnected.\n");
        } else if (scores == null) {
            return;
        } else if (winner.equals(nickname)) {
            ClearablePrinter.printAndClearNext("You are the winner\n");
            printScores(scores);
        } else {
            ClearablePrinter.printAndClearNext("The winner is " + winner + "\n");
            printScores(scores);
        }

        ClearablePrinter.printAndClearNext("You can now close the game.\n");
    }

    /**
     * Prints the lost connection error message.
     */
    public static void printLostConnection() {
        ClearablePrinter.printAndClearNext("Lost connection to server.\n");
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
        ClearablePrinter.printAndClearNext(nickname + " just connected\n");
    }

    /**
     * Prints the player's nickname that has disconnected.
     * @param nickname The nickname of the player.
     */
    public static void printPlayerJustDisconnected(String nickname){
        ClearablePrinter.printAndClearNext(nickname + " just disconnected\n");
    }

    /**
     * Prints the list of available games.
     * @param games The list of games.
     */
    public static void printGamesList(List<GameInfo> games){
        ClearablePrinter.printAndClearNext("Select a game from the list:\n");
        for (GameInfo info : games) {
            ClearablePrinter.printAndClearNext(info.toString());
        }
    }

    /**
     * Prints the number of connected players and how many players have to connect.
     * @param missing Number of players missing.
     * @param connectedPlayers Number of connected players.
     */
    public static void printNumberMissingPlayers(int missing, Collection<String> connectedPlayers){
        if (connectedPlayers == null) return;

        ClearablePrinter.printAndClearNext("Connected players: \n");
        for (String player : connectedPlayers) {
            ClearablePrinter.printAndClearNext(player + "\n");
        }

        if (missing == 1)
            ClearablePrinter.printAndClearNext("Waiting for " + missing + " player ...\n");
        else
            ClearablePrinter.printAndClearNext("Waiting for " + missing + " players ...\n");
    }

    /**
     * Prints the start game message.
     */
    public static void printStartGame(){
        ClearablePrinter.printAndClearNext("Game starts!\n");
    }

    /**
     * Prints the exit message.
     */
    public static void printExit(){
        ClearablePrinter.printAndClearNext("Digit '/exit' to close the launcher.\n");
    }

    public static void printReconnection(String myNickname, String reconnectedNickname) {
        String re = (!reconnectedNickname.equals(myNickname)) ? reconnectedNickname : "you";

        ClearablePrinter.printAndClearNext(ANSI_RED + re + " reconnected!!!" + ANSI_RESET + "\n");
    }

    public static void printDisconnected(String disconnectedPlayer) {
        ClearablePrinter.printAndClearNext(ANSI_RED + disconnectedPlayer + " disconnected!!!" + ANSI_RESET + "\n");
    }
}
