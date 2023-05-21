package it.polimi.ingsw;

import java.util.Arrays;

public class AppLauncher {
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
