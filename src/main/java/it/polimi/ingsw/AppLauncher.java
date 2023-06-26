package it.polimi.ingsw;

import java.util.Arrays;

/**
 * Starting class of the application. It chooses whether to start the server or a client.
 */
public class AppLauncher {
    /**
     * Main method: it starts either the server or the client.
     * @param args Command Line arguments.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.exit(1);
        }

        if (args[0].equalsIgnoreCase("server")) {
            Server.launch(Arrays.stream(args).skip(1).toList());
        } else if (args[0].equalsIgnoreCase("client")) {
            Client.launch(Arrays.stream(args).skip(1).toList());
        }
    }
}
