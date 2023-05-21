package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientStatus;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.InputReceiver;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.client.cli.TextInterfacePrinter.*;

public class TextInterface extends ClientView implements InputReceiver {
    private boolean inChat;
    private ClientStatus status;

    public TextInterface() {
        super();
        status = ClientStatus.LOGIN;
        printWelcomeMessage();
    }

    @Override
    public void start() {
        InputHandler inputHandler = new InputHandler(this);
        (new Thread(inputHandler)).start();
    }

    @Override
    public void onGamesList(GamesList message) {
        synchronized (System.out) {
            List<GameInfo> games = message.getAvailable();

            if (games == null) {
                printLoginFailed();
                return;
            }

            onAcceptedAction(new AcceptedAction(true, AcceptedActionTypes.LOGIN));

            clearScreen();

            if (games.size() == 0) {
                printNoAvailableGames();
            } else {
                System.out.println("Select a game from the list:");
                for (GameInfo info : games) {
                    System.out.println(info);
                }
            }
        }
    }

    public void onAcceptedAction(AcceptedAction message) {
        synchronized (System.out) {
            if (!message.getAccepted()) {
                printDeniedAction();
                return;
            }
            switch (message.getType()) {
                case CREATE_GAME, SELECT_GAME, SELECT_LIVINGROOM, INSERT_BOOKSHELF, WRITE_CHAT -> status = ClientStatus.GAME;
                case LOGIN -> status = ClientStatus.CREATE_OR_SELECT_GAME;
            }
        }
    }

    @Override
    public void onItemsSelected(ItemsSelected message) {
        synchronized (System.out) {
            itemsChosen = message.getItems();
            if (itemsChosen == null) printInvalidSelection();
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
    public void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
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
                printChat(chat);
            }
        }
    }

    @Override
    public void onStartTurnUpdate(StartTurnUpdate update) {
        synchronized (System.out) {
            currentPlayer = update.getCurrentPlayer();

            if (!inChat && !endGame) {
                clearScreen();
                printChat(chat);
            }
        }
    }

    @Override
    public void onEndGameUpdate(EndGameUpdate update) {
        endGame = true;
        winner = update.getWinner();
        if(inChat)
            exitChat();

        clearScreen();
        printEndGame(nickname, winner, scores);
    }

    @Override
    public void login(Nickname message) {
        String nickname = message.getNickname();
        if (status != ClientStatus.LOGIN){
            printWrongStatus();
            return;
        }

        if(!isValidNickname(nickname)){
            printIncorrectNickname();
            return;
        }

        this.nickname = nickname;
        clientConnection.login(new Nickname(nickname));
    }

    @Override
    public void createGame(GameInitialization message) {

        if (status != ClientStatus.CREATE_OR_SELECT_GAME){
            printWrongStatus();
            return;
        }
        clientConnection.createGame(message);
    }

    @Override
    public void selectGame(GameSelection message) {
        if (status != ClientStatus.CREATE_OR_SELECT_GAME){
            printWrongStatus();
            return;
        }
        clientConnection.selectGame(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        clientConnection.selectFromLivingRoom(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        clientConnection.insertInBookshelf(message);
    }

    @Override
    public void writeChat(ChatMessage message) {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        clientConnection.writeChat(message);
    }

    @Override
    public void enterChat() {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        printInChat();
        inChat = true;
        clearScreen();
        printChat(chat);
    }

    @Override
    public void exitChat() {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        if (!inChat){
            printNotInChat();
            return;
        }
        inChat = false;
        clearScreen();
        printExitChat();
        if (endGame) printEndGame(nickname, winner, scores);
        else printGameRep();
    }

    private void printGameRep() {
        System.out.println("the current player is " + currentPlayer);

        printLivingRoom(livingRoom);
        printBookshelves(bookshelves);
        printPersonalGoalCard(personalGoalCard);
        printCommonGoalCards(commonGoalCards);
        printItemsChosen(itemsChosen);
        printEndingToken(endingToken);
        printScores(scores);

        System.out.flush();
    }

    public void getStatus() {
        System.out.println(status);
    } // this method will be deleted probably
}
