package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.Sender;

public interface Receiver {
    void receive(Object object, Sender sender);
}
