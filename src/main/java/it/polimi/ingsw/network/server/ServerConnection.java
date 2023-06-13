package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.view.server.ServerInputViewInterface;

/**
 * Interface used to represent a connection to (and from) a client on the server-side.
 */
public interface ServerConnection {
    /**
     * Sets the listener of the connection. It will be notified as new messages from the client.
     * @param serverInputViewInterface the listener.
     */
    void setServerInputViewInterface(ServerInputViewInterface serverInputViewInterface);

    void send(Message message);
}
