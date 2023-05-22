package it.polimi.ingsw;

import it.polimi.ingsw.view.client.cli.TextInterface;
import it.polimi.ingsw.view.client.gui.GUIInterface;

import java.util.Arrays;

public class AppLauncher {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.exit(1);
        }

        if (args[0].equalsIgnoreCase("server")) {
            Server.launch(Arrays.stream(args).skip(1).toList());
        } else if (args[0].equalsIgnoreCase("client")) {
            if (args[1].equalsIgnoreCase("gui"))
                GUIInterface.startGUI();
            else if (args[1].equalsIgnoreCase("cli")) {
                new TextInterface().start();
            }
            Client.launch(Arrays.stream(args).skip(1).toList());
        }
    }
}
