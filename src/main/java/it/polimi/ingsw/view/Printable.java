package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;

import java.util.List;
import java.util.Map;

public class Printable implements ViewInterface{
    @Override
    public void printGamesList() {

    }

    @Override
    public void printAcceptedAction(boolean isAccepted) {

    }

    @Override
    public void printBookshelves(Map<String, Item[][]> bookshelves) {
        for(Map.Entry<String, Item[][]> entry: bookshelves.entrySet()){
            System.out.println(entry.getKey() + "'s bookshelf:");
            printBookshelf(entry.getValue());
        }
        System.out.println();
    }

    //da definire bene
    @Override
    public void printBoard(Item[][] livingroom, Map<String, Item[][]> bookshelves, Map<String, Integer> scores) {
        printScores(scores);
        printBookshelves(bookshelves);
        printLivingroom(livingroom);
    }

    @Override
    public void printWaitingUpdate(int numberMissingPlayers) {
        System.out.println("Number of missing players: " + numberMissingPlayers);
    }

    @Override
    public void printCommonGoalCard(List<int[]> commonGoalCards) {

    }

    @Override
    public void printPersonalGoalCard(int personalGoalCard) {

    }

    @Override
    public void printChat(List<Message> chat) {

    }

    @Override
    public void printStartTurn(String username) {
        System.out.println("It's " + username + "'s turn");
    }

    @Override
    public void printEndGame() {

    }

    @Override
    public void printTileSelectionResponse(boolean response) {

    }

    @Override
    public void printScores(Map<String, Integer> scores) {
        System.out.println("Scoreboard: ");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    @Override
    public void printBookshelf(Item[][] array) {
        for(int i= array.length-1; i>=0; i--){
            for(int j=0; j< array[i].length; j++) {
                printItem(array[i][j]);
                System.out.println();
            }
        }
        System.out.println();
    }

    @Override
    public void printItem(Item item) {
        if (item == null) {
            System.out.print("  ");
        } else {
            switch (item) {
                case CAT -> System.out.print((char) 27 + "[32mC ");
                case BOOK -> System.out.print((char) 27 + "[37mB ");
                case GAME -> System.out.print((char) 27 + "[33mG ");
                case FRAME -> System.out.print((char) 27 + "[34mF ");
                case CUP -> System.out.print((char) 27 + "[36mC ");
                case PLANT -> System.out.print((char) 27 + "[35mP ");
            }
        }
    }

    @Override
    public void printLivingroom(Item[][] livingroom) {
        System.out.println("Livingroom: ");
        for (Item[] items : livingroom) {
            for (Item item : items) {
                printItem(item);
                System.out.println();
            }
        }
        System.out.println();
    }
}
