package it.polimi.ingsw.view.client;

import it.polimi.ingsw.view.InputViewInterface;

public interface InputReceiver extends InputViewInterface {
    void enterChat();
    void exitChat();
    void getStatus();
}
