package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Position;

import java.util.List;

public interface RequestSender {
    void login(String nickname);
    void selectGame(String nickname, int id);
    void createGame(String nickname, int numberPlayers, int numberCommonGoals);
    void selectFromLivingRoom(String nickname, List<Position> position);
    void putInBookshelf(String nickname, int column, List<Integer> permutation);
    void addMessage(String nickname, String text);
}
