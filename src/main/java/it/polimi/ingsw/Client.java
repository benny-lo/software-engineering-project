package it.polimi.ingsw;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.cli.TextInterface;

import java.util.List;

import static java.lang.Integer.parseInt;

public class Client {
    public static void launch(List<String> args) {
        ClientView view = new TextInterface();
        try {
            if (args.get(0).equalsIgnoreCase("rmi")) {
                if (args.size() == 1)
                    view.startRMI(ServerSettings.getHostName(), ServerSettings.getRmiPort());
                else if (args.size() == 3)
                    view.startRMI(args.get(1), parseInt(args.get(2)));
            } else if (args.get(0).equalsIgnoreCase("tcp")) {
                if (args.size() == 1)
                    view.startTCP(ServerSettings.getHostName(), ServerSettings.getSocketPort());
                else if (args.size() == 3)
                    view.startTCP(args.get(1), parseInt(args.get(2)));
            } else {
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.exit(1);
        }

        view.start();
    }
}
