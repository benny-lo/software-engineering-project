package it.polimi.ingsw.view.client.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientStatus;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.InputReceiver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static it.polimi.ingsw.view.client.cli.TextInterfacePrinter.*;

public class TextInterface extends ClientView implements InputReceiver {
    private boolean inChat;
    private ClientStatus status;

    /**
     * Constructor for the class.
     */
    public TextInterface() {
        super();
        status = ClientStatus.LOGIN;
        printWelcomeMessage();
    }

    /**
     * Override of the start method, starts a new thread, using inputHandler
     */
    @Override
    public void start() {
        InputHandler inputHandler = new InputHandler(this);
        (new Thread(inputHandler)).start();
    }

    /**
     * Override of the onGamesList method, prints the list of Games that are available, or error messages in cases the nickname
     * is not set, or there are no games available
     * @param message - message of the type GameList
     */
    @Override
    public void onGamesList(GamesList message) {
        synchronized (System.out) {
            List<GameInfo> games = message.getAvailable();

            if (games == null) {
                printLoginFailed();
                nickname = null;
                return;
            }

            status = ClientStatus.CREATE_OR_SELECT_GAME;

            clearScreen();

            if (games.size() == 0) {
                printNoAvailableGames();
            } else {
                System.out.println("Select a game from the list:");
                for (GameInfo info : games) {
                    System.out.println(info);
                }
                System.out.flush();
            }
        }
    }

    /**
     * Override of the onAcceptedInsertion method, handles what the text interface should do in case of a AcceptedInsertion message.
     * @param message - message of acceptedInsertion type, which is true if the insertion is accepted or false otherwise.
     */
    @Override
    public void onAcceptedInsertion(AcceptedInsertion message) {
        synchronized (System.out){
            if (!message.isAccepted()){
                printDeniedAction();
            }
            printGameRep();
        }
    }

    /**
     * Override of the onChatAccepted method, handles what the text interface should do in case of a chatAccepted message.
     * @param message - message of chatAccepted type, which is true if the player has connected to the chat or false otherwise.
     */
    @Override
    public void onChatAccepted(ChatAccepted message) {
        synchronized (System.out){
            if (!message.isAccepted()){
                printDeniedAction();
            }
        }
    }
    /**
     * Override of the onItemsSelected method, handles what the text interface should do in case of a ItemsSelected message.
     * @param message - message of onItemsSelected type, which contains the item chosen.
     */
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
    /**
     * Override of the onLivingRoomUpdate method, handles what the text interface should do in case of a LivingRoom update.
     * @param update - message of LivingRoomUpdate type, which contains the updated Living Room.
     */
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
    /**
     * Override of the onBookshelfUpdate method, handles what the text interface should do in case of a Bookshelf update.
     * @param update - message of BookshelfUpdate type, which contains the updated Bookshelf.
     */
    @Override
    public void onBookshelfUpdate(BookshelfUpdate update) {
        synchronized (System.out) {
            Map<Position, Item> ups = update.getBookshelf();
            for (Position p : ups.keySet()) {
                if (!bookshelves.containsKey(update.getOwner())) bookshelves.put(update.getOwner(), new Item[bookshelvesRows][bookshelvesColumns]);
                bookshelves.get(update.getOwner())[p.getRow()][p.getColumn()] = ups.get(p);
            }

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }
    /**
     * Override of the onWaitingUpdate method, handles what the text interface should do in case of a waiting update.
     * @param update- message of WaitingUpdate type, which is true if the player has just connected and the rest of the players are
     * waiting for the game to start.
     */
    @Override
    public void onWaitingUpdate(WaitingUpdate update) {
        synchronized (System.out) {
            System.out.println(update.getJustConnected() + " just connected");
            if (update.getMissing() != 0) System.out.println("Waiting for " + update.getMissing() + " players ...");
            else System.out.println("Game starts!");
        }
    }

    /**
     * Override of the onScoresUpdate method, handles what the text interface should do in case of a Scores update.
     * @param update - message of ScoresUpdate type, which is contains the updated scores.
     */
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

    /**
     * Override of the onEndingTokenUpdate method, handles what the text interface should do in case of a EndingToken update.
     * @param update - message of EndingTokenUpdate type, which contains the owner of the endingToken
     */
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

    /**
     * Override of the onCommonGoalCardsUpdate method, handles what the text interface should do in case of a CommonGoalCards update.
     * @param update - message of CommonGoalCardsUpdate type , which contains the updated common goal cards
     */
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
    /**
     * Override of the onPersonalGoalCardUpdate method, handles what the text interface should do in case of a PersonalGoalCard update.
     * @param update - message of PersonalGoalCardUpdate type , which contains the updated personal goal card
     */
    @Override
    public void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        synchronized (System.out) {
            int id = update.getId();
            PersonalGoalPattern personalGoalPattern;

            String filename = "/configuration/personalGoalCards/personal_goal_pattern_" + id + ".json";
            Gson gson = new GsonBuilder().serializeNulls()
                    .setPrettyPrinting()
                    .disableJdkUnsafe()
                    .enableComplexMapKeySerialization()
                    .create();
            try(Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))) {
                personalGoalPattern = gson.fromJson(reader, new TypeToken<PersonalGoalPattern>(){}.getType());
            } catch(IOException e) {
                printPersonalGoalCardConfigurationFailed();
                return;
            }

            Map<Position, Item> map = personalGoalPattern.getMaskPositions();
            for (Position position : map.keySet()){
                personalGoalCard[position.getRow()][position.getColumn()] = map.get(position);
            }

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    /**
     * Override of the onChatUpdate method, handles what the text interface should do in case of a chat update.
     * @param message - message of ChatUpdate type, which contains an updated chat message
     */
    @Override
    public void onChatUpdate(ChatUpdate message) {
        synchronized (System.out) {
            chat.add(message);

            if (inChat && !endGame) {
                clearScreen();
                printChat(chat);
            }
        }
    }

    /**
     * Override of the onStartTurnUpdate, handles what the text interface should do in case of a startTurn update.
     * @param update - message of StartTurnUpdate type, which contains the updated current player.
     */
    @Override
    public void onStartTurnUpdate(StartTurnUpdate update) {
        synchronized (System.out) {
            currentPlayer = update.getCurrentPlayer();

            if (!inChat && !endGame) {
                clearScreen();
                printGameRep();
            }
        }
    }

    /**
     * Override of the onEndGameUpdate, handles what the text interface should do in case of an endgame update.
     * @param update - message of the EndGameUpdate type, which contains the winner of the game.
     */
    @Override
    public void onEndGameUpdate(EndGameUpdate update) {
        endGame = true;
        winner = update.getWinner();
        if(inChat)
            exitChat();
        status = ClientStatus.ENDED_GAME;
        clearScreen();
        printEndGame(nickname, winner, scores);
    }

    /**
     * Override of the onGameData, handles what the text interface should do in case of a gameData update.
     * @param gameData - message of the GameData type, which contains information of the game, number of players, number
     * of common goal cards, of the row and columns of the living room and bookshelves.
     */
    @Override
    public void onGameData(GameData gameData) {
        synchronized (System.out) {
            if (gameData.getNumberPlayers() == -1 ||
                    gameData.getNumberCommonGoalCards() == -1 ||
                    gameData.getBookshelvesColumns() == -1 ||
                    gameData.getBookshelvesRows() == -1 ||
                    gameData.getLivingRoomColumns() == -1 ||
                    gameData.getLivingRoomRows() == -1) {
                printDeniedAction();
                return;
            }

            status = ClientStatus.GAME;

            numberPlayers = gameData.getNumberPlayers();
            numberCommonGoalCards = gameData.getNumberCommonGoalCards();
            livingRoom = new Item[gameData.getLivingRoomRows()][gameData.getLivingRoomColumns()];
            bookshelvesRows = gameData.getBookshelvesRows();
            bookshelvesColumns = gameData.getBookshelvesColumns();
            personalGoalCard = new Item[bookshelvesRows][bookshelvesColumns];
        }
    }

    /**
     * Override of the onDisconnection method, handles what the text interface should do in a case of a disconnection.
     */
    @Override
    public void onDisconnection() {
        if (status == ClientStatus.ERROR) return;
        if (status != ClientStatus.ENDED_GAME) clearScreen();
        status = ClientStatus.ERROR;
        printLostConnection();
        System.exit(0);
    }

    /**
     * Override of the login method, handles what the text interface should do in case of a login.
     * @param message message containing the chosen nickname.
     */
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
        clientConnection.send(new Nickname(nickname));
    }

    /**
     * Override of the createGame method, handles what the text interface should do in case of a gameInitialization message.
     * @param message message containing the information about the game to create.
     */
    @Override
    public void createGame(GameInitialization message) {

        if (status != ClientStatus.CREATE_OR_SELECT_GAME){
            printWrongStatus();
            return;
        }
        clientConnection.send(message);
    }

    /**
     * Override of the selectGame method, handles what the text interface should do in case of a GameSelection message.
     * @param message message containing the id of the game chosen.
     */
    @Override
    public void selectGame(GameSelection message) {
        if (status != ClientStatus.CREATE_OR_SELECT_GAME){
            printWrongStatus();
            return;
        }
        clientConnection.send(message);
    }

    /**
     * Override of the selectFromLivingRoom method, handles what the text interface should do in case of a LivingRoomSelection message.
     * @param message message containing the chosen positions.
     */
    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        clientConnection.send(message);
    }

    /**
     * * Override of the insertInBookshelf method, handles what the text interface should do in case of a BookshelfInsertion message.
     * @param message message containing the column and the order in which to insert the chosen tiles.
     */
    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        clientConnection.send(message);
    }

    /**
     * * Override of the writeChat method, handles what the text interface should do in case of a Chat message.
     * @param message message containing the text written.
     */
    @Override
    public void writeChat(ChatMessage message) {
        if (status != ClientStatus.GAME){
            printWrongStatus();
            return;
        }
        clientConnection.send(message);
    }

    /**
     * * Override of the enterChat method, handles what the text interface should do in case of a player entering the chat.
     */
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

    /**
     * * Override of the exitChat method, handles what the text interface should do in case of a player exiting the chat.
     */
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

    /**
     * This method prints the text interface representation of the whole game.
     */
    private void printGameRep() {
        printCurrentPlayer(currentPlayer);

        printLivingRoom(livingRoom);
        printBookshelves(bookshelves);
        printPersonalGoalCard(personalGoalCard);
        printCommonGoalCards(commonGoalCards);
        printItemsChosen(itemsChosen, currentPlayer);
        printEndingToken(endingToken);
        printScores(scores);

        System.out.flush();
    }

    public void getStatus() {
        System.out.println(status);
    } // this method will be deleted probably
}
