package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.message.Message;

/**
 * Interface used to represent a connection to (and from) a client on the server-side.
 */
public interface ClientConnection {
    /**
     * Send a {@code Message} to the server.
     * @param message the message to send.
     */
    void send(Message message);
}
