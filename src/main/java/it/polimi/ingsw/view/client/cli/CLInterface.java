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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static it.polimi.ingsw.view.client.cli.CLInterfacePrinter.*;

/**
 * Class representing the CLI.
 * It receives messages from the server and updates the text interface.
 * It sends the client's input to the server.
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
    private final List<GameInfo> games;

    /**
     * Constructor of the class: creates a {@code Map} for the bookshelves, commonGoalCards, and the scores;
     * and creates an {@code Array} for the chat and the games.
     */
    public CLInterface() {
        bookshelves = new HashMap<>();
        commonGoalCards = new HashMap<>();
        scores = new HashMap<>();
        chat = new ArrayList<>();
        games = new ArrayList<>();
    }

    /**
     * Starts the CLI, and it creates a new {@code Thread} in {@code InputHandler}.
     * It synchronizes on {@code this}.
     */
    @Override
    public synchronized void start() {
        status = CLIStatus.LOGIN;

        printWelcomeMessage();
        InputHandler inputHandler = new InputHandler(this);
        (new Thread(inputHandler)).start();
    }

    /**
     * {@inheritDoc}
     * Prints the list of Games that are available, but if the login failed prints an error.
     * It synchronizes on {@code this}.
     * @param message The message to process.
     */
    @Override
    public synchronized void onGamesList(GamesList message) {
        if (message.getAvailable() == null) {
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
     * {@inheritDoc}
     * Prints the GameRep if the insertion is successful, otherwise it prints an error.
     * It synchronizes on {@code this}.
     * @param message The message to process.
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
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param message The message to process.
     */
    @Override
    public synchronized void onChatAccepted(ChatAccepted message) {
        if (!message.isAccepted()) {
            printDeniedAction();
        }
        System.out.flush();
    }

    /**
     * {@inheritDoc}
     * Prints the GameRep if the selection is successful, otherwise it prints an error.
     * It synchronizes on {@code this}.
     * @param message The message to process.
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
     * {@inheritDoc}
     * Prints the GameRep and updates the LivingRoom.
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onLivingRoomUpdate(LivingRoomUpdate update) {
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
     * {@inheritDoc}
     * Prints the GameRep and updates the bookshelf.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Prints the StartGame message if all players have connected, otherwise prints how many player are missing.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Prints the GameRep.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Prints the GameRep and assign the ending token.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Prints the GameRep and updates the CommonGoalCards.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Prints the GameRep and updates the PersonalGoalCard.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Add a message to the Chat and prints the Chat.
     * It synchronizes on {@code this}.
     * @param message The update to process.
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
     * {@inheritDoc}
     * Updates the current player and prints the GameRep.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Ends the Game, prints the winner and the ranking.
     * It synchronizes on {@code this}.
     * @param update The update to process.
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
     * {@inheritDoc}
     * Sets the connected players, the LivingRoom, the Bookshelves, the PersonalGoalCard if the game selection is successful,
     * otherwise prints an error.
     * It synchronizes on {@code this}.
     * @param gameData The message to process.
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
     * {@inheritDoc}
     * Prints the Disconnection message when the client loses his connection with the server.
     * It synchronizes on {@code this}.
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
     * {@inheritDoc}
     * Sets the client's nickname, if the nickname is invalid prints IncorrectNickname message.
     * Sends to {@code ClientConnection} a Nickname message.
     * It synchronizes on {@code this}.
     * @param message Message containing the chosen nickname.
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
     * {@inheritDoc}
     * Sends to {@code ClientConnection} a GameInitialization message.
     * It synchronizes on {@code this}.
     * @param message Message containing the information about the game to create.
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
     * {@inheritDoc}
     * Sends to {@code ClientConnection} a GameSelection message.
     * It synchronizes on {@code this}.
     * @param message Message containing the id of the game chosen.
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
     * {@inheritDoc}
     * Sends to {@code ClientConnection} a LivingRoomSelection message.
     * It synchronizes on {@code this}.
     * @param message Message containing the chosen positions.
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
     * {@inheritDoc}
     * Sends to {@code ClientConnection} a BookshelfInsertion message.
     * It synchronizes on {@code this}.
     * @param message Message containing the column and the order in which to insert the chosen tiles.
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
     * {@inheritDoc}
     * Sends to {@code ClientConnection} a ChatMessage message.
     * It synchronizes on {@code this}.
     * @param message Message containing the text written.
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
     * {@inheritDoc}
     * The client enters the Chat.
     * It synchronizes on {@code this}.
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
     * {@inheritDoc}
     * The client exits the Chat.
     * It synchronizes on {@code this}.
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

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     */
    @Override
    public synchronized void exit() {
        System.exit(0);
    }

    /**
     * Prints the text interface representation of the whole game.
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
