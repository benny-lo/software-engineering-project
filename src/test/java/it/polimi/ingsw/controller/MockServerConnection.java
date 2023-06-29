package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.view.server.ServerInputViewInterface;


import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Class mocking a {@code ServerConnection}, it is used only in tests.
 */
public class MockServerConnection implements ServerConnection {

    /**
     * Queue that contains all messages sent to the client.
     */
    public Queue<Message> queue;

    /**
     * Constructor of the class: initializes {@code queue} as empty.
     */
    public MockServerConnection() {
        queue = new ArrayDeque<>();
    }

    /**
     * It's not implemented, because it doesn't receive messages from the network.
     * @param serverInputViewInterface the listener.
     */
    @Override
    public void setServerInputViewInterface(ServerInputViewInterface serverInputViewInterface) {}

    /**
     * Sends a {@code Message}: it's added to {@code queue}.
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        queue.add(message);
    }
}
