package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.action.ActionListener;

public interface ControllerInterface extends ActionListener {
    boolean isStarted();
    int getNumberPlayers();
    int getNumberCommonGoalCards();
    int getNumberActualPlayers();
}
