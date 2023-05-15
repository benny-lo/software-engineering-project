package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Position;

import java.util.List;

public interface InputReceiver {
    void login(String nickname);
    void createGame(int numberPlayers, int numberCommonGoalCards);
    void joinGame(int id);
    void livingRoom(List<Position> positions);
    void bookshelf(int column, List<Integer> permutation);
    void enterChat();
    void message(String text);
    void exitChat();
}
