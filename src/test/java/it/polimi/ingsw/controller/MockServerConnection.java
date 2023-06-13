package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.view.server.ServerInputViewInterface;


import java.util.ArrayList;
import java.util.List;

public class MockServerConnection implements ServerConnection {

    public List<Message> list;
    public MockServerConnection() {
        list = new ArrayList<>();
    }

    @Override
    public void setServerInputViewInterface(ServerInputViewInterface serverInputViewInterface) {

    }

    @Override
    public void send(Message message) {
        list.add(message);
    }
}
