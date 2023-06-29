package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.InputViewInterface;
import it.polimi.ingsw.view.client.gui.controllers.*;

import java.util.List;

/**
 * Interface used by the GUI controllers.
 */
public interface GUIViewInterface extends InputViewInterface {
    /**
     * Set the login controller in the {@code GUInterface}.
     * @param controller The controller reference.
     */
    void setController(LoginController controller);

    /**
     * Set the lobby controller in the {@code GUInterface}.
     * @param controller The controller reference.
     */
    void setController(LobbyController controller);

    /**
     * Set the waiting room controller in the {@code GUInterface}.
     * @param controller The controller reference.
     */
    void setController(WaitingRoomController controller);

    /**
     * Set the game controller in the {@code GUInterface}.
     * @param controller The controller reference.
     */
    void setController(GameController controller);

    /**
     * Set the chat controller in the {@code GUInterface}.
     * @param controller The controller reference.
     */
    void setController(ChatController controller);

    /**
     * Getter of the client's nickname.
     * @return The nickname of the client.
     */
    String getNickname();

    /**
     * Getter of the others client's nicknames.
     * @return The nicknames of the other clients.
     */
    List<String> getOthersNicknames();
}
