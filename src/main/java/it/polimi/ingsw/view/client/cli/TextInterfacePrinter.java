package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;

import java.util.List;
import java.util.Map;

class TextInterfacePrinter {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    static void printCurrentPlayer(String currentPlayer){
        if (currentPlayer == null)
            return;
        System.out.println(ANSI_RED + "The current player is " + currentPlayer + ANSI_RESET + "\n");
    }

    static void printWelcomeMessage(){
        System.out.println("""
                Welcome to MyShelfie!
                Digit '/help' for all the commands!""");
    }

    static void printHelp(){
        System.out.println("""
                Commands list:\s
                \t /login [nickname]\s
                \t /create_game [number_of_players] [number_of_common_goal_cards]
                \t /select_game [id]\s
                \t /living_room [positions]\s
                \t /bookshelf [column] [permutation]\s
                \t /enter_chat\s
                \t /exit_chat""");
    }

    static void printWrongCommand() {
        System.out.println("This command does not exists! Try to digit '/help' for other commands.");
    }

    static void printIncorrectCommand(){
        System.out.println("This command is misspelled! Try to digit '/help' for other commands.");
    }
    static void printWrongStatus(){
        System.out.println("You cannot do this action now!\nTry to digit '/help' for other commands.");
    }

    static void printLoginFailed(){
        System.out.println("Login failed. Try again!");
    }

    static void printNoAvailableGames(){
        System.out.println("There are no available games.\nCreate a new game with the command '/create_game [number_of_players] [number_of_common_goal_cards]'.");
    }

    static void printIncorrectNickname(){
        System.out.println("""
                This nickname is incorrect! Retry to login.
                Your nickname has to be at least 1 character and less than 30.
                It can only contains alphanumeric characters and underscores.""");
    }

    static void printInvalidSelection(){
        System.out.println("Invalid tile selection! Try again.");
    }

    static void printDeniedAction(){
        System.out.println("Action denied. Try again!");
    }

    static void printInChat(){
        System.out.println("You've now entered in the chat!");
    }
    static void printNotInChat(){
        System.out.println("You've not entered in the chat yet!\nTry with the command '/enter_chat'.");
    }

    static void printExitChat(){
        System.out.println("You've now left the chat!");
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void printLivingRoom(Item[][] livingRoom) {
        if (livingRoom == null) return;

        System.out.println("LivingRoom: ");
        System.out.println("  0 1 2 3 4 5 6 7 8");
        int i = 0;
        for (Item[] items : livingRoom) {
            System.out.print(i + " ");
            i++;
            for (Item item : items) {
                printItem(item);
            }
            System.out.println();
        }
        System.out.println();
    }


    static void printItem(Item item) {
        if (item == null) {
            System.out.print(ANSI_RED + "X " + ANSI_RESET);
            return;
        }

        switch (item) {
            case CAT -> System.out.print(ANSI_GREEN + "C " + ANSI_RESET);
            case BOOK -> System.out.print(ANSI_WHITE + "B " + ANSI_RESET);
            case GAME -> System.out.print(ANSI_YELLOW + "G " + ANSI_RESET);
            case FRAME -> System.out.print(ANSI_BLUE + "F " + ANSI_RESET);
            case CUP -> System.out.print(ANSI_CYAN + "T " + ANSI_RESET);
            case PLANT -> System.out.print(ANSI_PURPLE + "P " + ANSI_RESET);
            case LOCKED -> System.out.print(ANSI_RED + "X " + ANSI_RESET);
        }
    }


    static void printBookshelves(Map<String, Item[][]> bookshelves) {
        if (bookshelves == null)
            return;
        for (Map.Entry<String, Item[][]> entry : bookshelves.entrySet()) {
            System.out.println(entry.getKey() + "'s bookshelf:");
            printBookshelfOrPersonalGoalCard(entry.getValue());
        }
    }

    static void printBookshelfOrPersonalGoalCard(Item[][] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            System.out.print(i + " ");
            for (int j = 0; j < array[i].length; j++) {
                printItem(array[i][j]);
            }
            System.out.println();
        }
        System.out.println("  0 1 2 3 4");
        System.out.println();
    }

    static void printPersonalGoalCard(Item[][] personalGoalCard) {
        if (personalGoalCard == null)
            return;
        System.out.println("Your personal goal card is: ");
        printBookshelfOrPersonalGoalCard(personalGoalCard);
    }

    static void printCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        if (commonGoalCards == null)
            return;
        String description="";
        System.out.println("Your common goal cards are: ");
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            switch (card.getKey()){
                case 0 -> description="\nTwo groups each containing 4 tiles of the same type in a 2x2 square.\nThe tiles of one square can be different from those of the other square.\n";
                case 1 -> description="\nTwo columns each formed by 6 different types of tiles.\n";
                case 2 -> description="\nFour groups each containing at least 4 tiles of the same type.\nThe tiles of one group can be different from those of another group.\n";
                case 3 -> description="\nSix groups each containing at least 2 tiles of the same type\nThe tiles of one group can be different from those of another group.\n";
                case 4 -> description="\nThree columns each formed by 6 tiles of maximum three different types.\nOne column can show the same or a different combination of another column\n";
                case 5 -> description="\nTwo lines each formed by 5 different types of tiles. One line can\nshow the same or a different combination of the other line.\n";
                case 6 -> description="\nFour lines each formed by 5 tiles of maximum three different types. One \nline can show the same or a different combination of another line.\n";
                case 7 -> description="\nFour tiles of the same type in the four corners of the bookshelf.\n";
                case 8 -> description="\nEight or more tiles of the same type with no restrictions about the position of these tiles.\n";
                case 9 -> description="\nFive tiles of the same type forming an X.\n";
                case 10 -> description="\nFive tiles of the same type forming a diagonal\n";
                case 11 -> description="\nFive columns of increasing or decreasing height.\nStarting from the first column on the left or on the right,\neach next column must be made of exactly one more tile. Tiles can be of any type.\n";
            }
            System.out.println("Id: " + card.getKey() + ", Top token value: " + card.getValue() + description);
        }
    }

    static void printItemsChosen(List<Item> itemsChosen, String currentPlayer) {
        if (currentPlayer == null) return;
        if (itemsChosen == null) return;
        System.out.print(currentPlayer + " chose the items: ");
        for (Item item : itemsChosen) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    static void printEndingToken(String endingToken) {
        if (endingToken != null) {
            System.out.println(endingToken + " has the ending token");
        }
    }

    static void printScores(Map<String, Integer> scores) {
        if (scores == null) return;
        System.out.println("Rankings:");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    static void printChat(List<Message> chat) {
        if (chat.size() == 0) {
            System.out.println("No message in chat yet");
            return;
        }

        for (Message message : chat) {
            System.out.println(message.getNickname() + " wrote: " + message.getText());
        }
        System.out.flush();
    }

    static void printEndGame(String nickname, String winner, Map<String, Integer> scores) {
        if (winner == null)
            return;
        else if (scores == null)
            return;
        else if (winner.equals(nickname)) {
            System.out.println("You are the winner");
        } else {
            System.out.println("The winner is " + winner);
        }
        printScores(scores);
        System.out.flush();
    }

    static void printLostConnection(){
        System.out.println("Lost connection to server. Shutting down ...");
    }

    static void printPersonalGoalCardConfigurationFailed(){
        System.err.println("""
                    Configuration file for personalGoalCard not found.
                    The configuration file should be in configuration/personalGoalCard
                    with name personal_goal_pattern_{id}""");
    }
}
