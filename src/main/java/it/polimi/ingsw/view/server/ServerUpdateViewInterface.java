package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.UpdateViewInterface;

public interface ServerUpdateViewInterface extends UpdateViewInterface {
    String getNickname();
    void setNickname(String nickname);
    void setController(Controller controller);
    boolean isInGame();
}
