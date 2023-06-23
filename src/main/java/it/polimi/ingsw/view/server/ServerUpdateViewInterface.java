package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.view.UpdateViewInterface;

public interface ServerUpdateViewInterface extends UpdateViewInterface {
    String getNickname();
    void setNickname(String nickname);
    void setController(ControllerInterface controller);
    boolean isInGame();
}
