package it.polimi.ingsw.view.server;

import it.polimi.ingsw.view.InputViewInterface;

/**
 * Interface receiving inputs over the network.
 */
public interface ServerInputViewInterface extends InputViewInterface {
    /**
     * Processes a network disconnection.
     */
    void disconnect();
}
