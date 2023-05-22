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

    static void printWelcomeMessage(){
        System.out.println("""
                Welcome to MyShelfie!
                Digit '/help' for all the commands!""");
    }

    static void printHelp(){
        System.out.println("Commands list: \n\t /login [nickname] \n\t /create_game [number_of_players] [number_of_common_goal_cards] \n\t /select_game [id] \n\t /living_room [positions] \n\t /bookshelf [column] [permutation] \n\t /enter_chat \n\t /exit_chat");

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
        System.out.println("There are no available games. Create a new game with the command '/create_game [number_of_players] [number_of_common_goal_cards]'.");
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
    }


    static void printItem(Item item) {
        if (item == null) {
            System.out.print(ANSI_RED + "\u2717 " + ANSI_RESET);
            return;
        }

        switch (item) {
            case CAT -> System.out.print(ANSI_GREEN + "C " + ANSI_RESET);
            case BOOK -> System.out.print(ANSI_WHITE + "B " + ANSI_RESET);
            case GAME -> System.out.print(ANSI_YELLOW + "G " + ANSI_RESET);
            case FRAME -> System.out.print(ANSI_BLUE + "F " + ANSI_RESET);
            case CUP -> System.out.print(ANSI_CYAN + "T " + ANSI_RESET);
            case PLANT -> System.out.print(ANSI_PURPLE + "P " + ANSI_RESET);
            case LOCKED -> System.out.print(ANSI_RED + "\u2717 " + ANSI_RESET);
        }
    }


    static void printBookshelves(Map<String, Item[][]> bookshelves) {
        for (Map.Entry<String, Item[][]> entry : bookshelves.entrySet()) {
            System.out.println(entry.getKey() + "'s bookshelf:");
            printBookshelf(entry.getValue());
        }
        System.out.println();
    }

    static void printBookshelf(Item[][] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            System.out.print(i + " ");
            for (int j = 0; j < array[i].length; j++) {
                printItem(array[i][j]);
            }
            System.out.println();
        }
        System.out.println("  0 1 2 3 4");
    }

    static void printPersonalGoalCard(int personalGoalCard) {
        if (personalGoalCard == 0) return;
        System.out.println("Your personal goal card is " + personalGoalCard);
    }

    static void printCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        System.out.println("Your common goal cards are: ");
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            System.out.println("id: " + card.getKey() + " top: " + card.getValue());
        }
    }

    static void printItemsChosen(List<Item> itemsChosen) {
        if (itemsChosen == null) return;
        System.out.print("You chose the items: ");
        for (Item item : itemsChosen) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    static void printEndingToken(String endingToken) {
        if (endingToken == null) {
            System.out.println("Nobody has the ending token");
        } else {
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
        if (winner.equals(nickname)) {
            System.out.println("You are the winner");
        } else {
            System.out.println("The winner is " + winner);
        }
        printScores(scores);
        System.out.flush();
    }
}
