package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.Receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Receiver receiver;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket socket, Receiver receiver) {
        this.socket = socket;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            Object input;

            while(true) {
                input = in.readObject();
                receiver.receive(input);
            }
        } catch (IOException e) {
            System.err.println("failed to get streams");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("deserialization failed");
        }
    }
}
