package it.polimi.ingsw;

import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.cli.CLInterface;
import it.polimi.ingsw.view.client.gui.GUInterface;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Class starting the client.
 */
public class Client {
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final int DEFAULT_SOCKET_PORT = 1234;
    private static ClientView view = null;

    /**
     * Instantiates the {@code ClientView} with correct type and starts either the rmi or tcp connection.
     * @param args {@code List} of arguments from command line.
     */
    public static void launch(List<String> args) {
        if (args.size() == 0) System.exit(1);
        try {
            if (args.get(0).equalsIgnoreCase("cli")) {
                view = new CLInterface();
                startNetwork(args.subList(1, args.size()));
            } else if (args.get(0).equalsIgnoreCase("gui")) {
                view = new GUInterface();
                startNetwork(args.subList(1, args.size()));
            } else {
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        view.start();
    }

    private static void startNetwork(List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).equalsIgnoreCase("rmi")) {
                view.startRMI(DEFAULT_SERVER_ADDRESS, DEFAULT_RMI_PORT);
            } else if (args.get(0).equalsIgnoreCase("tcp")) {
                view.startTCP(DEFAULT_SERVER_ADDRESS, DEFAULT_SOCKET_PORT);
            }
        }
        else if (args.size() == 3) {
            if (args.get(0).equalsIgnoreCase("rmi")) {
                view.startRMI(args.get(1), parseInt(args.get(2)));
            } else if (args.get(0).equalsIgnoreCase("tcp")) {
                view.startTCP(args.get(1), parseInt(args.get(2)));
            }
        } else {
            Logger.clientError("wrong arguments : client {cli | gui} {tcp | rmi} [{hostName} {numberPort}]");
            System.exit(1);
        }
    }
}
