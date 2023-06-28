package it.polimi.ingsw.view.client.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.InputReceiver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static it.polimi.ingsw.view.client.cli.CLInterfacePrinter.*;

/**
 * This class receives the messages from the server and updates the text interface. It also sends the client's input to the server.
 */

public class CLInterface extends ClientView implements InputReceiver {
    private CLIStatus status;
    private Collection<String> connectedPlayers;
    private final Map<String, Item[][]> bookshelves;
    private String endingToken;
    private Item[][] personalGoalCard;
    private final Map<Integer, Integer> commonGoalCards;
    private final Map<String, Integer> scores;
    private boolean endGame;
    private Item[][] livingRoom;
    private final List<ChatUpdate> chat;
    private List<Item> chosenItems;
    private String currentPlayer;
    private int bookshelvesRows;
    private int bookshelvesColumns;
    private final List<GameInfo> games = new ArrayList<>();

    /**
     * Constructor for the class.
     */
    public CLInterface() {
        bookshelves = new HashMap<>();
        commonGoalCards = new HashMap<>();
        scores = new HashMap<>();
        chat = new ArrayList<>();
    }

    /**
     * Override of the start method, starts a new thread, using inputHandler
     */
    @Override
    public synchronized void start() {
        status = CLIStatus.LOGIN;

        printWelcomeMessage();
        InputHandler inputHandler = new InputHandler(this);
        (new Thread(inputHandler)).start();
    }

    /**
     * Override of the onGamesList method, prints the list of Games that are available, or error messages in cases the nickname
     * is not set, or there are no games available
     * @param message - message of the type GameList
     */
    @Override
    public synchronized void onGamesList(GamesList message) {
        if (message == null) {
            printLoginFailed();
            System.out.flush();
            nickname = null;
            return;
        }

        for (GameInfo game : message.getAvailable()) {
            List<GameInfo> other = games.stream().filter((g) -> g.getId() != game.getId()).toList();
            games.removeAll(other);
            if (game.getNumberPlayers() != -1 && game.getNumberCommonGoals() != -1) {
                games.add(game);
            }
        }

        clearScreen();

        status = CLIStatus.LOBBY;

        if (games.size() == 0)
            printNoAvailableGames();
        else
            printGamesList(games);

        System.out.flush();
    }

    /**
     * Override of the onAcceptedInsertion method, handles what the text interface should do in case of a AcceptedInsertion message.
     * @param message - message of acceptedInsertion type, which is true if the insertion is accepted or false otherwise.
     */
    @Override
    public synchronized void onAcceptedInsertion(AcceptedInsertion message) {
        if (!message.isAccepted()){
            printDeniedAction();
        }else {
            printGameRep();
        }
        System.out.flush();
    }

    /**
     * Override of the onChatAccepted method, handles what the text interface should do in case of a chatAccepted message.
     * @param message - message of chatAccepted type, which is true if the player has connected to the chat or false otherwise.
     */
    @Override
    public synchronized void onChatAccepted(ChatAccepted message) {
        if (!message.isAccepted()) {
            printDeniedAction();
        }
        System.out.flush();
    }
    /**
     * Override of the onItemsSelected method, handles what the text interface should do in case of a ItemsSelected message.
     * @param message - message of onItemsSelected type, which contains the item chosen.
     */
    @Override
    public synchronized void onSelectedItems(SelectedItems message) {
        chosenItems = message.getItems();
        if (chosenItems == null) printInvalidSelection();
        else if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }
        System.out.flush();
    }
    /**
     * Override of the onLivingRoomUpdate method, handles what the text interface should do in case of a LivingRoom update.
     * @param update - message of LivingRoomUpdate type, which contains the updated Living Room.
     */
    @Override
    public void onLivingRoomUpdate(LivingRoomUpdate update) {
        Map<Position, Item> ups = update.getLivingRoomUpdate();
        for (Position p : ups.keySet()) {
            livingRoom[p.getRow()][p.getColumn()] = ups.get(p);
        }

        if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }
        System.out.flush();
    }
    /**
     * Override of the onBookshelfUpdate method, handles what the text interface should do in case of a Bookshelf update.
     * @param update - message of BookshelfUpdate type, which contains the updated Bookshelf.
     */
    @Override
    public synchronized void onBookshelfUpdate(BookshelfUpdate update) {
        Map<Position, Item> ups = update.getBookshelf();
        for (Position p : ups.keySet()) {
            if (!bookshelves.containsKey(update.getOwner())) bookshelves.put(update.getOwner(), new Item[bookshelvesRows][bookshelvesColumns]);
            bookshelves.get(update.getOwner())[p.getRow()][p.getColumn()] = ups.get(p);
        }

        if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }
        System.out.flush();
    }
    /**
     * Override of the onWaitingUpdate method, handles what the text interface should do in case of a waiting update.
     * @param update- message of WaitingUpdate type, which is true if the player has just connected and the rest of the players are
     * waiting for the game to start.
     */
    @Override
    public synchronized void onWaitingUpdate(WaitingUpdate update) {
        if (update.isConnected()){
            printPlayerJustConnected(update.getNickname());
            connectedPlayers.add(update.getNickname());
        } else {
            printPlayerJustDisconnected(update.getNickname());
            connectedPlayers.remove(update.getNickname());
        }

        if (update.getMissing() != 0) printNumberMissingPlayers(update.getMissing(), connectedPlayers);
        else printStartGame();

        System.out.flush();
    }

    /**
     * Override of the onScoresUpdate method, handles what the text interface should do in case of a Scores update.
     * @param update - message of ScoresUpdate type, which is contains the updated scores.
     */
    @Override
    public synchronized void onScoresUpdate(ScoresUpdate update) {
        scores.putAll(update.getScores());

        if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }

        System.out.flush();
    }

    /**
     * Override of the onEndingTokenUpdate method, handles what the text interface should do in case of a EndingToken update.
     * @param update - message of EndingTokenUpdate type, which contains the owner of the endingToken
     */
    @Override
    public synchronized void onEndingTokenUpdate(EndingTokenUpdate update) {
        endingToken = update.getOwner();

        if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }

        System.out.flush();
    }

    /**
     * Override of the onCommonGoalCardsUpdate method, handles what the text interface should do in case of a CommonGoalCards update.
     * @param update - message of CommonGoalCardsUpdate type , which contains the updated common goal cards
     */
    @Override
    public synchronized void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        Map<Integer, Integer> cardsChanged = update.getCommonGoalCardsUpdate();
        for (Integer id : cardsChanged.keySet()) {
            commonGoalCards.put(id, cardsChanged.get(id));
        }

        if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }

        System.out.flush();
    }

    /**
     * Override of the onPersonalGoalCardUpdate method, handles what the text interface should do in case of a PersonalGoalCard update.
     * @param update - message of PersonalGoalCardUpdate type , which contains the updated personal goal card
     */
    @Override
    public synchronized void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
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

        if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }

        System.out.flush();
    }

    /**
     * Override of the onChatUpdate method, handles what the text interface should do in case of a chat update.
     * @param message - message of ChatUpdate type, which contains an updated chat message
     */
    @Override
    public synchronized void onChatUpdate(ChatUpdate message) {
        chat.add(message);

        if (status == CLIStatus.CHAT) {
            clearScreen();
            printChat(chat);
        }

        System.out.flush();
    }

    /**
     * Override of the onStartTurnUpdate, handles what the text interface should do in case of a startTurn update.
     * @param update - message of StartTurnUpdate type, which contains the updated current player.
     */
    @Override
    public synchronized void onStartTurnUpdate(StartTurnUpdate update) {
        currentPlayer = update.getCurrentPlayer();
        chosenItems = null;

        if (status != CLIStatus.CHAT) {
            clearScreen();
            printGameRep();
        }

        System.out.flush();
    }

    /**
     * Override of the onEndGameUpdate, handles what the text interface should do in case of an endgame update.
     * @param update - message of the EndGameUpdate type, which contains the winner of the game.
     */
    @Override
    public synchronized void onEndGameUpdate(EndGameUpdate update) {
        endGame = true;
        if (status == CLIStatus.CHAT) {
            exitChat();
        }
        status = CLIStatus.ENDED_GAME;
        clearScreen();
        printEndGame(nickname, update.getWinner(), scores);
        printExit();

        System.out.flush();
    }

    /**
     * Override of the onGameData, handles what the text interface should do in case of a gameData update.
     * @param gameData - message of the GameData type, which contains information of the game, number of players, number
     * of common goal cards, of the row and columns of the living room and bookshelves.
     */
    @Override
    public synchronized void onGameData(GameData gameData) {
        if (gameData.getNumberPlayers() == -1 ||
                gameData.getNumberCommonGoalCards() == -1 ||
                gameData.getConnectedPlayers() == null ||
                gameData.getBookshelvesColumns() == -1 ||
                gameData.getBookshelvesRows() == -1 ||
                gameData.getLivingRoomColumns() == -1 ||
                gameData.getLivingRoomRows() == -1) {
            printDeniedAction();
            System.out.flush();
            return;
        }

        status = CLIStatus.GAME;

        connectedPlayers = gameData.getConnectedPlayers();
        livingRoom = new Item[gameData.getLivingRoomRows()][gameData.getLivingRoomColumns()];
        bookshelvesRows = gameData.getBookshelvesRows();
        bookshelvesColumns = gameData.getBookshelvesColumns();
        personalGoalCard = new Item[bookshelvesRows][bookshelvesColumns];
        System.out.flush();
    }

    /**
     * Override of the onDisconnection method, handles what the text interface should do in a case of a disconnection.
     */
    @Override
    public synchronized void onDisconnection() {
        if (status == CLIStatus.ERROR) return;
        clearScreen();
        status = CLIStatus.ERROR;
        printLostConnection();
        printExit();
        System.out.flush();
    }

    /**
     * Override of the login method, handles what the text interface should do in case of a login.
     * @param message message containing the chosen nickname.
     */
    @Override
    public void login(Nickname message) {
        synchronized (this) {
            if (status != CLIStatus.LOGIN) {
                printWrongStatus();
                System.out.flush();
                return;
            }

            if (isNicknameValid(message.getNickname())) {
                printIncorrectNickname();
                System.out.flush();
                return;
            }

            this.nickname = message.getNickname();
        }
        super.login(new Nickname(nickname));
    }

    /**
     * Override of the createGame method, handles what the text interface should do in case of a gameInitialization message.
     * @param message message containing the information about the game to create.
     */
    @Override
    public void createGame(GameInitialization message) {
        synchronized (this) {
            if (status != CLIStatus.LOBBY) {
                printWrongStatus();
                System.out.flush();
                return;
            }
        }
        super.createGame(message);
    }

    /**
     * Override of the selectGame method, handles what the text interface should do in case of a GameSelection message.
     * @param message message containing the id of the game chosen.
     */
    @Override
    public void selectGame(GameSelection message) {
        synchronized (this) {
            if (status != CLIStatus.LOBBY) {
                printWrongStatus();
                System.out.flush();
                return;
            }
        }
        super.selectGame(message);
    }

    /**
     * Override of the selectFromLivingRoom method, handles what the text interface should do in case of a LivingRoomSelection message.
     * @param message message containing the chosen positions.
     */
    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        synchronized (this) {
            if (status != CLIStatus.GAME) {
                printWrongStatus();
                System.out.flush();
                return;
            }
        }
        super.selectFromLivingRoom(message);
    }

    /**
     * * Override of the insertInBookshelf method, handles what the text interface should do in case of a BookshelfInsertion message.
     * @param message message containing the column and the order in which to insert the chosen tiles.
     */
    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        synchronized (this) {
            if (status != CLIStatus.GAME) {
                printWrongStatus();
                System.out.flush();
                return;
            }
        }
        super.insertInBookshelf(message);
    }

    /**
     * * Override of the writeChat method, handles what the text interface should do in case of a Chat message.
     * @param message message containing the text written.
     */
    @Override
    public void writeChat(ChatMessage message) {
        synchronized (this) {
            if (status != CLIStatus.CHAT) {
                printWrongStatus();
                System.out.flush();
                return;
            }
        }
        super.writeChat(message);
    }

    /**
     * * Override of the enterChat method, handles what the text interface should do in case of a player entering the chat.
     */
    @Override
    public synchronized void enterChat() {
        if (status != CLIStatus.GAME) {
            printWrongStatus();
            System.out.flush();
            return;
        }
        status = CLIStatus.CHAT;
        clearScreen();
        printInChat();
        printChat(chat);
        System.out.flush();
    }

    /**
     * * Override of the exitChat method, handles what the text interface should do in case of a player exiting the chat.
     */
    @Override
    public synchronized void exitChat() {
        if (status != CLIStatus.CHAT) {
            printNotInChat();
            System.out.flush();
            return;
        }
        status = CLIStatus.GAME;
        clearScreen();
        printExitChat();
        if (!endGame) printGameRep();
        System.out.flush();
    }

    @Override
    public synchronized void exit() {
        System.exit(0);
    }

    /**
     * This method prints the text interface representation of the whole game.
     */
    private void printGameRep() {
        printCurrentPlayer(nickname, currentPlayer);

        printLivingRoom(livingRoom);
        printBookshelves(bookshelves);
        printPersonalGoalCard(personalGoalCard);
        printCommonGoalCards(commonGoalCards);
        printItemsChosen(chosenItems, currentPlayer);
        printEndingToken(endingToken);
        printScores(scores);

        System.out.flush();
    }
}
