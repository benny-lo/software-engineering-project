package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

import java.util.List;

public interface ControllerInterface {
    void join(ServerUpdateViewInterface view);
    void livingRoom(List<Position> positions, ServerUpdateViewInterface view);
    void bookshelf(int column, List<Integer> permutation, ServerUpdateViewInterface view);
    void chat(String text, String receiver, ServerUpdateViewInterface view);
    void disconnection(ServerUpdateViewInterface view);
    boolean isStarted();
    int getNumberPlayers();
    int getNumberCommonGoalCards();
    int getNumberActualPlayers();
}
