package it.polimi.ingsw.network.client.socket;


import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private final Socket socket;
    private final ObjectReceiver receiver;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerHandler(Socket socket, ObjectReceiver receiver) {
        this.socket = socket;
        this.receiver = receiver;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch(IOException e) {
            System.err.println("failed to get I/O");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Object input;
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                input = in.readObject();
                receiver.receive(input);
            }
        } catch (IOException e) {
            System.err.println("RequestSenderTCP: line 28 = I/O failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("RequestSenderTCP: line 31 = unknown class");
        }
    }

    public synchronized void send(NetworkMessageWithSender message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("RequestSenderTCP: line 41 = I/O failed");
            e.printStackTrace();
        }
    }
}
