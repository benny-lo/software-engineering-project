package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;

import java.util.List;
import java.util.Map;

public interface ViewInterface {
    public void printGamesList();
    public void printAcceptedAction(boolean isAccepted);
    public void printBookshelves(Map<String, Item[][]> bookshelves);
    public void printBoard(Item[][] livingroom, Map<String, Item[][]> bookshelves, Map<String, Integer> scores);
    public void printWaitingUpdate(int numberMissingPlayers);
    public void printCommonGoalCard(List<int[]> commonGoalCards);
    public void printPersonalGoalCard(int personalGoalCard);
    public void printChat(List<Message> chat);
    public void printStartTurn(String username);
    public void printEndGame();
    public void printTileSelectionResponse(boolean response);
    public void printScores(Map<String, Integer> scores);
    public void printBookshelf(Item[][] array);
    public void printItem(Item item);
    public void printLivingroom(Item[][] livingroom);
}
