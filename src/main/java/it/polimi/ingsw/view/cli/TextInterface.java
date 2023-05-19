package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.utils.networkMessage.client.*;
import it.polimi.ingsw.utils.networkMessage.server.*;
import it.polimi.ingsw.view.ClientView;

import java.util.List;
import java.util.Map;

public class TextInterface extends ClientView implements InputReceiver {
    private boolean inChat;

    public TextInterface() {
        super();
        System.out.println("Welcome to MyShelfie");
    }

    public void start() {
        InputHandler inputHandler = new InputHandler(this);
        (new Thread(inputHandler)).start();
    }

    @Override
    public void onGamesList(GamesList message) {
        synchronized (System.out) {
            List<GameInfo> games = message.getAvailable();

            if (games == null) {
                System.out.println("Action denied. Try again!");
                return;
            }

            clearScreen();

            for (GameInfo info : games) {
                System.out.println("Id " + info.getId() + ":\n" +
                        "\tNumber of players: " + info.getNumberPlayers() + "\n" +
                        "\tNumber of common goal cards: " + info.getNumberCommonGoals());
            }

            if (games.size() == 0) {
                System.out.println("There are no available games. Create a new one (Number of player and number of CommonGoalCard)");
            }
        }
    }

    @Override
    public void onAcceptedAction(AcceptedAction message) {
        synchronized (System.out) {
            if (!message.getAccepted()) {
                System.out.println("Action denied. Try again!");
            }
        }
    }

    @Override
    public void onItemsSelected(ItemsSelected message) {
        synchronized (System.out) {
            itemsChosen = message.getItems();
            if (itemsChosen == null) System.out.println("Invalid tile selection! Try again.");

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onLivingRoomUpdate(LivingRoomUpdate update) {
        synchronized (System.out) {
            Map<Position, Item> ups = update.getLivingRoomUpdate();
            for (Position p : ups.keySet()) {
                livingRoom[p.getRow()][p.getColumn()] = ups.get(p);
            }

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    @Override
    public void onBookshelfUpdate(BookshelfUpdate update) {
        synchronized (System.out) {
            Map<Position, Item> ups = update.getBookshelf();
            for (Position p : ups.keySet()) {
                if (!bookshelves.containsKey(update.getOwner())) bookshelves.put(update.getOwner(), new Item[6][5]);
                bookshelves.get(update.getOwner())[p.getRow()][p.getColumn()] = ups.get(p);
            }

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
            if (update.getMissing() != 0) System.out.println("Waiting for " + update.getMissing() + " players ...");
            else System.out.println("Game starts!");
        }
    }

    @Override
    public void onScoresUpdate(ScoresUpdate update) {
        synchronized (System.out) {
            for (String nick : update.getScores().keySet()) {
                scores.put(nick, update.getScores().get(nick));
            }

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
    public void onCommonGoalCardUpdate(CommonGoalCardsUpdate update) {
        synchronized (System.out) {
            Map<Integer, Integer> cardsChanged = update.getCommonGoalCardsUpdate();
            for (Integer id : cardsChanged.keySet()) {
                commonGoalCards.put(id, cardsChanged.get(id));
            }

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
        sender.login(new Nickname(nickname));
    }

    @Override
    public void createGame(int numberPlayers, int numberCommonGoalCards) {
        sender.createGame(new GameInitialization(numberPlayers, numberCommonGoalCards));
    }

    @Override
    public void selectGame(int id) {
        sender.selectGame(new GameSelection(id));
    }

    @Override
    public void livingRoom(List<Position> positions) {
        sender.selectFromLivingRoom(new LivingRoomSelection(positions));
    }

    @Override
    public void bookshelf(int column, List<Integer> permutation) {
        sender.insertInBookshelf(new BookshelfInsertion(column, permutation));
    }

    @Override
    public void message(String text) {
        sender.writeChat(new ChatMessage(text));
    }

    @Override
    public void enterChat() {
        inChat = true;
        clearScreen();
        printChat();
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

        printLivingRoom();
        printBookshelves();
        printPersonalGoalCard();
        printCommonGoalCards();
        printItemsChosen();
        printEndingToken();
        printScores();

        System.out.flush();
    }

    public void printItem(Item item) {
        if (item == null) {
            System.out.print("  ");
            return;
        }

        switch (item) {
            case CAT -> System.out.print((char) 27 + "[32mC ");
            case BOOK -> System.out.print((char) 27 + "[37mB ");
            case GAME -> System.out.print((char) 27 + "[33mG ");
            case FRAME -> System.out.print((char) 27 + "[34mF ");
            case CUP -> System.out.print((char) 27 + "[36mC ");
            case PLANT -> System.out.print((char) 27 + "[35mP ");
            case LOCKED -> System.out.print("  ");
        }
    }

    private void printLivingRoom() {
        if (livingRoom == null) return;

        System.out.println("LivingRoom: ");
        for (Item[] items : livingRoom) {
            for (Item item : items) {
                printItem(item);
            }
            System.out.println();
        }
    }

    private void printBookshelves() {
        for (Map.Entry<String, Item[][]> entry : bookshelves.entrySet()) {
            System.out.println(entry.getKey() + "'s bookshelf:");
            printBookshelf(entry.getValue());
        }
        System.out.println();
    }

    private void printBookshelf(Item[][] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            for (int j = 0; j < array[i].length; j++) {
                printItem(array[i][j]);
            }
            System.out.println();
        }
    }

    private void printPersonalGoalCard() {
        if (personalGoalCard == 0) return;
        System.out.println("Your personal goal card is " + personalGoalCard);
    }

    private void printCommonGoalCards() {
        System.out.println("your common goal cards are:");
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            System.out.println("id: " + card.getKey() + " top: " + card.getValue());
        }
    }

    private void printItemsChosen() {
        if (itemsChosen == null) return;
        System.out.print("You chose the items: ");
        for (Item item : itemsChosen) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    private void printEndingToken() {
        if (endingToken == null) {
            System.out.println("Nobody has the ending token");
        } else {
            System.out.println(endingToken + " has the ending token");
        }
    }

    private void printScores() {
        if (scores == null) return;
        System.out.println("rankings:");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    private void printChat () {
        if (chat.size() == 0) {
            System.out.println("No message in chat yet");
            return;
        }

        for (Message message : chat) {
            System.out.println(message.getText() + "wrote: " + message.getText());
        }
        System.out.flush();
    }

    private void printEndGame () {
        if (winner.equals(nickname)) {
            System.out.println("You are the winner");
        } else {
            System.out.println("The winner is " + winner);
        }
        printScores();
        System.out.flush();
    }
}
