package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.cli.TextInterface;

public class Client {
    public static void main(String[] args) {
        ClientView view = new TextInterface();
        if (args[0].equals("RMI")) {
            view.startRMI();
        } else if (args[0].equals("TCP")) {
            view.startTCP();
        } else {
            System.exit(1);
        }
    }
}
