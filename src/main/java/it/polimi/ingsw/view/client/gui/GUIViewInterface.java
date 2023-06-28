package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.InputViewInterface;
import it.polimi.ingsw.view.client.gui.controllers.AbstractController;

import java.util.List;

public interface GUIViewInterface extends InputViewInterface {
    void receiveController(AbstractController controller);
    String getNickname();
    List<String> getOthersNicknames();
}
