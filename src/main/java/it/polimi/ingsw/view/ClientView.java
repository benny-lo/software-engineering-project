package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientView {
    private int numberPlayers;
    private int numberCommonGoalCards;
    private Item[][] livingRoom;
    private final Map<String, Item[][]> bookshelves;
    private boolean endingToken;
    private int personalGoalCard;
    private final List<int[]> commonGoalCards;
    private Map<String, Integer> scores;
    private boolean endGame;
    private final List<Message> chat;

    public ClientView() {
        bookshelves = new HashMap<>();
        commonGoalCards = new ArrayList<>();
        endingToken = false;
        endGame = false;
        chat = new ArrayList<>();
    }

    public void setLivingRoom(Item[][] livingRoom) {
        this.livingRoom = livingRoom;
    }

    public void setBookshelf(String nickname, Item[][] bookshelf) {
        bookshelves.put(nickname, bookshelf);
    }

    public void setEndingToken() {
        endingToken = true;
    }

    public void setPersonalGoalCard(int id) {
        personalGoalCard = id;
    }

    public void setCommonGoalCards(int id, int top) {
        for(int[] card : commonGoalCards) {
            if (card[0] == id) {
                card[1] = top;
                return;
            }
        }

        int[] card = new int[2];
        card[0] = id;
        card[1] = top;
        commonGoalCards.add(card);
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public void setEndGame() {
        endGame = true;
    }

    public void setChatMessage(Message message) {
        chat.add(message);
    }
}
