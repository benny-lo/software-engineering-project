package it.polimi.ingsw;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.cli.TextInterface;
import it.polimi.ingsw.view.client.gui.GUIController;

import java.util.List;

import static java.lang.Integer.parseInt;

public class Client {
    public static void launch(List<String> args) {
        ClientView view = null;
        if (args.size() == 0) System.exit(1);
        try {
            if (args.get(0).equalsIgnoreCase("cli")) {
                view = new TextInterface();
                if (args.size() == 2) {
                    if (args.get(1).equalsIgnoreCase("rmi")) {
                        view.startRMI(ServerSettings.getHostName(), ServerSettings.getRmiPort());
                    } else if (args.get(1).equalsIgnoreCase("tcp")) {
                        view.startTCP(ServerSettings.getHostName(), ServerSettings.getSocketPort());
                    }
                }
                else if (args.size() == 4) {
                    if (args.get(1).equalsIgnoreCase("rmi")) {
                        view.startRMI(args.get(2), parseInt(args.get(3)));
                    } else if (args.get(1).equalsIgnoreCase("tcp")) {
                        view.startTCP(args.get(2), parseInt(args.get(3)));
                    }
                } else {
                    System.err.println("wrong cli arguments : [client] [cli] [hostName] [numberPort]");
                    System.exit(1);
                }
            } else if (args.get(0).equalsIgnoreCase("gui")) {
                view = new GUIController();
                if (args.size() == 2) {
                    if (args.get(1).equalsIgnoreCase("rmi")) {
                        view.startRMI(ServerSettings.getHostName(), ServerSettings.getRmiPort());
                    } else if (args.get(1).equalsIgnoreCase("tcp")) {
                        view.startTCP(ServerSettings.getHostName(), ServerSettings.getSocketPort());
                    }
                }
                else if (args.size() == 4) {
                    if (args.get(1).equalsIgnoreCase("rmi")) {
                        view.startRMI(args.get(2), parseInt(args.get(3)));
                    } else if (args.get(1).equalsIgnoreCase("tcp")) {
                        view.startTCP(args.get(2), parseInt(args.get(3)));
                    }
                } else {
                    System.err.println("wrong gui arguments : [client] [gui] [hostName] [numberPort]");
                    System.exit(1);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        view.start();
    }
}
