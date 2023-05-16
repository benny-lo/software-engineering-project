package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.utils.Rank;
import it.polimi.ingsw.utils.networkMessage.server.*;
import it.polimi.ingsw.view.ClientView;

import java.util.List;

public class TextInterface extends ClientView implements InputReceiver {
    private final InputHandler inputHandler;
    private boolean inChat;
    public TextInterface() {
        super();
        this.inputHandler = new InputHandler(this);
        (new Thread(inputHandler)).start();

        System.out.println("Welcome to MyShelfie");
    }

    @Override
    public void onGamesList(GamesList message) {
        synchronized (System.out) {
            List<GameInfo> games = message.getAvailable();

            if (games == null) {
                System.out.println("Try again!");
                return;
            }

            clearScreen();

            for (GameInfo info : games) {
                System.out.println("id " + info.getId() + ":\n" +
                        "\tnumber of players: " + info.getNumberPlayers() + "\n" +
                        "\tnumber of common goal cards: " + info.getNumberCommonGoals());
            }

            if (games.size() == 0) {
                System.out.println("there are no available games. create a new one");
            }
        }
    }

    @Override
    public void onAcceptedAction(AcceptedAction message) {
        synchronized (System.out) {
            if (!message.getAccepted()) {
                System.out.println("Try again!");
            }
        }
    }

    @Override
    public void onItemsSelected(ItemsSelected message) {
        synchronized (System.out) {
            itemsChosen = message.getItems();
            if (itemsChosen == null) System.out.println("Try again!");

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onLivingRoomUpdate(LivingRoomUpdate update) {
        synchronized (System.out) {
            livingRoom = update.getLivingRoom();

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onBookshelfUpdate(BookshelfUpdate update) {
        synchronized (System.out) {
            bookshelves.put(update.getOwner(), update.getBookshelf());

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onWaitingUpdate(WaitingUpdate update) {
        synchronized (System.out) {
            System.out.println(update.getJustConnected() + " just connected");
            System.out.println("waiting for " + update.getMissing() + "players ...");
        }
    }

    @Override
    public void onScoresUpdate(ScoresUpdate update) {
        synchronized (System.out) {
            scores = update.getScores();

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onEndingTokenUpdate(EndingTokenUpdate update) {
        synchronized (System.out) {
            endingToken = update.getOwner();

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onCommonGoalCardUpdate(CommonGoalCardUpdate update) {
        synchronized (System.out) {
            int[] card = new int[2];
            card[0] = update.getId();
            card[1] = update.getTop();

            commonGoalCards.add(card);

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        synchronized (System.out) {
            personalGoalCard = update.getId();

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onChatUpdate(ChatUpdate update) {
        synchronized (System.out) {
            chat.add(new Message(update.getNickname(), update.getText()));

            if (inChat && !endGame) {
                clearScreen();
                printChat();
            }
        }
    }

    @Override
    public void onStartTurnUpdate(StartTurnUpdate update) {
        synchronized (System.out) {
            currentPlayer = update.getCurrentPlayer();

            if (!inChat && !endGame) {
                clearScreen();
                printChat();
            }
        }
    }

    @Override
    public void onEndGameUpdate(EndGameUpdate update) {
        endGame = true;
        winner = update.getWinner();

        clearScreen();
        printEndGame();
    }

    @Override
    public void login(String nickname) {
        this.nickname = nickname;
        sender.login(nickname);
    }

    @Override
    public void createGame(int numberPlayers, int numberCommonGoalCards) {
        sender.createGame(nickname, numberPlayers, numberCommonGoalCards);
    }

    @Override
    public void joinGame(int id) {
        sender.selectGame(nickname, id);
    }

    @Override
    public void livingRoom(List<Position> positions) {
        sender.selectFromLivingRoom(nickname, positions);
    }

    @Override
    public void bookshelf(int column, List<Integer> permutation) {
        sender.putInBookshelf(nickname, column, permutation);
    }

    @Override
    public void enterChat() {
        inChat = true;
        clearScreen();
        printChat();
    }

    @Override
    public void message(String text) {
        sender.addMessage(nickname, text);
    }

    @Override
    public void exitChat() {
        inChat = false;
        clearScreen();
        if (endGame) printEndGame();
        else printGameRep();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printGameRep() {
        System.out.println("the current player is " + currentPlayer);
        System.out.println();

        printLivingRoom();
        printBookshelves();
        printPersonalGoalCard();
        printCommonGoalCards();
        printItemsChosen();
        printEndingToken();
        printScores();
    }

    private void printLivingRoom() {
        if (livingRoom == null) return;

        for (Item[] items : livingRoom) {
            for (Item item : items) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printBookshelves() {
        for(String nick : bookshelves.keySet()) {
            System.out.println(nick + " 's bookshelf:");
            for(int i = 0; i < bookshelves.get(nick).length; i++) {
                for(int j = 0; j < bookshelves.get(nick)[i].length; j++) {
                    System.out.print(bookshelves.get(nick)[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private void printPersonalGoalCard() {
        if (personalGoalCard == 0) return;
        System.out.println("your personal goal card is " + personalGoalCard);
        System.out.println();
    }

    private void printCommonGoalCards() {
        System.out.println("your common goal cards are:");
        for(int[] card : commonGoalCards) {
            System.out.println("id: " + card[0] + " top: " + card[1]);
        }
        System.out.println();
    }

    private void printItemsChosen() {
        if (itemsChosen == null) return;
        System.out.print("you chose the items: ");
        for(Item item : itemsChosen) {
            System.out.print(item + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printEndingToken() {
        if (endingToken == null) {
            System.out.println("nobody has the ending token");
        } else {
            System.out.println(endingToken + " has the ending token");
        }
        System.out.println();
    }

    private void printScores() {
        System.out.println("rankings:");
        for(Rank rank : scores) {
            System.out.println(rank.getNickname() + ": " + rank.getScore());
        }
        System.out.println();
    }

    private void printChat() {
        if (chat.size() == 0) {
            System.out.println("no message in chat yet");
            return;
        }

        for(Message message: chat) {
            System.out.println(message.getText() + "wrote " + message.getText());
        }
        System.out.println();
    }

    private void printEndGame() {
        if (winner.equals(nickname)) {
            System.out.println("You are the winner");
        } else {
            System.out.println("the winner is " + winner);
        }

        printScores();
    }
}
