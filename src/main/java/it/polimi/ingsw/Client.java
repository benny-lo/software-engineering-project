package it.polimi.ingsw;

import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.cli.TextInterface;

import java.util.List;

public class Client {
    public static void launch(List<String> args) {
        ClientView view = new TextInterface();
        if (args.get(0).equalsIgnoreCase("rmi")) {
            view.startRMI();
        } else if (args.get(0).equalsIgnoreCase("tcp")) {
            view.startTCP();
        } else {
            System.exit(1);
        }

        view.start();
    }
}
