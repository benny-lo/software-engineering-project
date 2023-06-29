package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.view.InputViewInterface;

/**
 * Interface that receives inputs from the CLI.
 */

public interface InputReceiver extends InputViewInterface {
    /**
     * The client enters the chat.
     */
    void enterChat();

    /**
     * The client quits the chat.
     */
    void exitChat();

    /**
     * The client closes the CLI.
     */
    void exit();
}
