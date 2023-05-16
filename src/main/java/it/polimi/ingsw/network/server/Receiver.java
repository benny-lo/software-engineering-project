package it.polimi.ingsw.network.server;

public interface Receiver {
    void receive(Object object, Sender sender);
}
