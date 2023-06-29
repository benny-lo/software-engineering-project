package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.view.UpdateViewInterface;

/**
 * Interface processing updates sent to the client from the server-side.
 */
public interface ServerUpdateViewInterface extends UpdateViewInterface {
    /**
     * Setter for the nickname.
     * @param nickname The nickname to set.
     */
    void setNickname(String nickname);

    /**
     * Getter for the nickname.
     * @return {@code String} corresponding to the nickname of {@code this}.
     */
    String getNickname();

    /**
     * Setter for the {@code Controller} that {@code this} joined.
     * @param controller The {@code Controller} joined.
     */
    void setController(ControllerInterface controller);

    /**
     * Checks whether {@code this} is logged in any game (has a {@code Controller}).
     * @return {@code true} iff the {@code Controller} of {@code this} is not {@code null}.
     */
    boolean isInGame();
}
