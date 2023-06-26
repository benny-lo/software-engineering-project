package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.view.server.ServerInputViewInterface;


import java.util.ArrayDeque;
import java.util.Queue;

public class MockServerConnection implements ServerConnection {

    public Queue<Message> queue;
    public MockServerConnection() {
        queue = new ArrayDeque<>();
    }

    @Override
    public void setServerInputViewInterface(ServerInputViewInterface serverInputViewInterface) {}

    @Override
    public void send(Message message) {
        queue.add(message);
    }
}
