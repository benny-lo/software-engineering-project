package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerInputViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class ServerConnectionRMI extends UnicastRemoteObject implements ServerConnection, ServerConnectionRMIInterface {
    private final Queue<Message> sendingQueue;
    private ServerInputViewInterface receiver;
    private final ClientConnectionRMIInterface client;
    private final Timer serverTimer;
    private final Timer clientTimer;
    private final Object beepLock;
    private Beep clientBeep;

    public ServerConnectionRMI(ClientConnectionRMIInterface client) throws RemoteException {
        super();
        this.sendingQueue = new ArrayDeque<>();
        this.client = client;
        this.serverTimer = new Timer();
        this.clientTimer = new Timer();
        this.beepLock = new Object();
    }

    public void start() {
        serverTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    client.receive(new Beep());
                } catch(RemoteException e) {
                    serverTimer.cancel();
                    clientTimer.cancel();
                    receiver.disconnect();
                }
            }
        }, 15000, 30000);

        clientTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (beepLock) {
                    if (clientBeep != null) {
                        clientBeep = null;
                        return;
                    }
                }
                receiver.disconnect();
            }
        }, 30000, 30000);

        (new Thread(() -> {
            Message message;
            while (true) {
                synchronized (sendingQueue) {
                    while (sendingQueue.isEmpty()) {
                        try {
                            sendingQueue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    message = sendingQueue.poll();
                }

                try {
                    if (message instanceof LivingRoomUpdate) {
                        client.receive((LivingRoomUpdate) message);
                    } else if (message instanceof BookshelfUpdate) {
                        client.receive((BookshelfUpdate) message);
                    } else if (message instanceof WaitingUpdate) {
                        client.receive((WaitingUpdate) message);
                    } else if (message instanceof ScoresUpdate) {
                        client.receive((ScoresUpdate) message);
                    } else if (message instanceof EndingTokenUpdate) {
                        client.receive((EndingTokenUpdate) message);
                    } else if (message instanceof CommonGoalCardsUpdate) {
                        client.receive((CommonGoalCardsUpdate) message);
                    } else if (message instanceof PersonalGoalCardUpdate) {
                        client.receive((PersonalGoalCardUpdate) message);
                    } else if (message instanceof ChatUpdate) {
                        client.receive((ChatUpdate) message);
                    } else if (message instanceof StartTurnUpdate) {
                        client.receive((StartTurnUpdate) message);
                    } else if (message instanceof EndGameUpdate) {
                        client.receive((EndGameUpdate) message);
                    } else if (message instanceof GamesList) {
                        client.receive((GamesList) message);
                    } else if (message instanceof ItemsSelected) {
                        client.receive((ItemsSelected) message);
                    } else if (message instanceof GameData) {
                        client.receive((GameData) message);
                    } else if (message instanceof AcceptedInsertion) {
                        client.receive((AcceptedInsertion) message);
                    } else if (message instanceof ChatAccepted) {
                        client.receive((ChatAccepted) message);
                    }
                } catch (RemoteException ignored) {}
            }
        })).start();
    }

    @Override
    public void setServerInputViewInterface(ServerInputViewInterface receiver) {
        this.receiver = receiver;
    }

    @Override
    public void login(Nickname message) throws RemoteException {
        receiver.login(message);
    }

    @Override
    public void createGame(GameInitialization message) throws RemoteException {
        receiver.createGame(message);
    }

    @Override
    public void selectGame(GameSelection message) throws RemoteException {
        receiver.selectGame(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) throws RemoteException {
        receiver.selectFromLivingRoom(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) throws RemoteException {
        receiver.insertInBookshelf(message);
    }

    @Override
    public void writeChat(ChatMessage message) throws RemoteException {
        receiver.writeChat(message);
    }

    @Override
    public void beep(Beep beep) throws RemoteException {
        synchronized (beepLock) {
            this.clientBeep = beep;
        }
    }

    @Override
    public void send(LivingRoomUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(BookshelfUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }

    }

    @Override
    public void send(WaitingUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(ScoresUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(EndingTokenUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(CommonGoalCardsUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(PersonalGoalCardUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(ChatUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(StartTurnUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(EndGameUpdate update) {
        synchronized (sendingQueue) {
            sendingQueue.add(update);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(GamesList gamesList) {
        synchronized (sendingQueue) {
            sendingQueue.add(gamesList);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(ItemsSelected itemsSelected) {
        synchronized (sendingQueue) {
            sendingQueue.add(itemsSelected);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(GameData gameData) {
       synchronized (sendingQueue) {
           sendingQueue.add(gameData);
           sendingQueue.notifyAll();
       }
    }

    @Override
    public void send(AcceptedInsertion acceptedInsertion) {
        synchronized (sendingQueue) {
            sendingQueue.add(acceptedInsertion);
            sendingQueue.notifyAll();
        }
    }

    @Override
    public void send(ChatAccepted chatAccepted) {
        synchronized (sendingQueue) {
            sendingQueue.add(chatAccepted);
            sendingQueue.notifyAll();
        }
    }
}
