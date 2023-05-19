package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.ServerReceiver;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;
import it.polimi.ingsw.utils.networkMessage.client.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectionTCP implements Runnable {
    private final Socket socket;
    private ServerReceiver receiver;
    private final ObjectOutputStream out;

    public ServerConnectionTCP(Socket socket) {
        this.socket = socket;

        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            System.err.println("ServerConnectionTCP, line 22: failed to get output stream");
            throw new RuntimeException(e);
        }

        this.out = out;
    }

    public void setReceiver(ServerReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run() {
        try {
            Object input;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                input = in.readObject();
                receive(input);
            }
        } catch (IOException e) {
            System.err.println("ServerConnectionTCP, line 38: IOException while reading from socket");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("ServerConnectionTCP, line 41: ClassNotFoundException while reading from socket");
            throw new RuntimeException(e);
        }
    }

    private void receive(Object input) {
        if (input instanceof Nickname) {
            receiver.login((Nickname) input);
        } else if (input instanceof GameInitialization) {
            receiver.createGame((GameInitialization) input);
        } else if (input instanceof GameSelection) {
            receiver.selectGame((GameSelection) input);
        } else if (input instanceof LivingRoomSelection) {
            receiver.selectFromLivingRoom((LivingRoomSelection) input);
        } else if (input instanceof BookshelfInsertion) {
            receiver.insertInBookshelf((BookshelfInsertion) input);
        } else if (input instanceof ChatMessage) {
            receiver.writeChat((ChatMessage) input);
        }
    }

    public void sendAsync(NetworkMessage message) {
        (new Thread(() -> send(message))).start();
    }

    private void send(NetworkMessage message) {
        synchronized (out) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("ServerConnectionTCP, line 76: failed to send to client");
                throw new RuntimeException(e);
            }
        }
    }
}
