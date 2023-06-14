package it.polimi.ingsw;

import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.cli.CLInterface;
import it.polimi.ingsw.view.client.gui.GUInterface;

import java.util.List;

import static java.lang.Integer.parseInt;

public class Client {
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final int DEFAULT_SOCKET_PORT = 1234;
    public static void launch(List<String> args) {
        ClientView view = null;
        if (args.size() == 0) System.exit(1);
        try {
            if (args.get(0).equalsIgnoreCase("cli")) {
                view = new CLInterface();
                if (args.size() == 2) {
                    if (args.get(1).equalsIgnoreCase("rmi")) {
                        view.startRMI(DEFAULT_SERVER_ADDRESS, DEFAULT_RMI_PORT);
                    } else if (args.get(1).equalsIgnoreCase("tcp")) {
                        view.startTCP(DEFAULT_SERVER_ADDRESS, DEFAULT_SOCKET_PORT);
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
                view = new GUInterface();
                if (args.size() == 2) {
                    if (args.get(1).equalsIgnoreCase("rmi")) {
                        view.startRMI(DEFAULT_SERVER_ADDRESS, DEFAULT_RMI_PORT);
                    } else if (args.get(1).equalsIgnoreCase("tcp")) {
                        view.startTCP(DEFAULT_SERVER_ADDRESS, DEFAULT_SOCKET_PORT);
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
            } else {
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        view.start();
    }
}
