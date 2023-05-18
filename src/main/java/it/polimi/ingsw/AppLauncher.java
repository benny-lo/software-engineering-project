package it.polimi.ingsw;

import java.util.Arrays;

public class AppLauncher {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.exit(1);
        }

        if (args[0].equalsIgnoreCase("server")) {
            Server server = new Server();
            server.launch(Arrays.stream(args).skip(1).toList());
        } else if (args[0].equalsIgnoreCase("client")) {
            Client client = new Client();
            client.launch(Arrays.stream(args).skip(1).toList());
        }
    }
}
