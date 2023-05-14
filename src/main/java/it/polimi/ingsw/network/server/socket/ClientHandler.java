package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.Sender;
import it.polimi.ingsw.network.server.Receiver;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable, Sender {
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
                receiver.receive(input, this);
            }
        } catch (IOException e) {
            System.err.println("failed to get streams");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("deserialization failed");
        }
    }

    @Override
    public void sendListOfGames(GamesList gamesList) {
        send(gamesList);
    }

    @Override
    public void sendItemsSelection(ItemsSelected itemsSelected) {
        send(itemsSelected);
    }

    @Override
    public void sendAccepted(AcceptedAction acceptedAction) {
        send(acceptedAction);
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {
        sendAsync(update);
    }

    private synchronized void send(NetworkMessage networkMessage) {
        try {
            out.writeObject(networkMessage);
        } catch (IOException e) {
            System.out.println("failed to send message");
            e.printStackTrace();
        }
    }

    private void sendAsync(NetworkMessage message) {
        (new Thread(() -> send(message))).start();
    }
}
