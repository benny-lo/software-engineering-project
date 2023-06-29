package it.polimi.ingsw.view.client;

import it.polimi.ingsw.view.UpdateViewInterface;

/**
 * Interface that updates the CLI/GUI if the client has disconnected.
 */
public interface ClientUpdateViewInterface extends UpdateViewInterface {
    /**
     * Updates the view when the client has disconnected.
     */
    void onDisconnection();
}
