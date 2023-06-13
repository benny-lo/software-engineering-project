package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.message.client.*;

/**
 * Interface used to represent a connection to (and from) a client on the server-side.
 */
public interface ClientConnection {
    /**
     * Sends a {@code Nickname} message to the server.
     * @param message the message to send.
     */
    void send(Nickname message);

    /**
     * Sends a {@code GameInitialization} message to the server.
     * @param message the message to send.
     */
    void send(GameInitialization message);

    /**
     * Sends a {@code GameSelection} message to the server.
     * @param message the message to send.
     */
    void send(GameSelection message);

    /**
     * Sends a {@code LivingRoomSelection} message to the server.
     * @param message the message to send.
     */
    void send(LivingRoomSelection message);

    /**
     * Sends a {@code BookshelfInsertion} message to the server.
     * @param message the message to send.
     */
    void send(BookshelfInsertion message);

    /**
     * Sends a {@code ChatMessage} message to the server.
     * @param message the message to send.
     */
    void send(ChatMessage message);
}
