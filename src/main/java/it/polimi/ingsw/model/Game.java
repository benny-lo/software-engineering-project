package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.modellistener.*;
import it.polimi.ingsw.model.board.BoardManager;
import it.polimi.ingsw.model.commongoalcard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalgoalcard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalgoalcard.PersonalGoalPattern;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * Class representing a game. It contains all managers and the nicknames of all players.
 */
public class Game implements GameInterface {
    private static final Random random = new Random();
    private final int numberPlayers;
    private final int numberCommonGoalCards;
    private String currentPlayer;
    private final Map<String, Player> players;
    private BoardManager boardManager;
    private CommonGoalCardManager commonGoalCardManager;
    private EndingTokenListener endingTokenListener;

    /**
     * Game's Constructor: it initializes {@code this} with a {@code List} of nicknames and a number of common goal
     * cards.
     * @param nicknames {@code List} containing the nicknames of the players.
     * @param numberCommonGoalCards Number of common goal cards. (It can be either to '1' or '2').
     * @throws IOException error occurred with I/O for JSON configuration files.
     */
    public Game(List<String> nicknames, int numberCommonGoalCards) throws IOException {
        this.numberPlayers = nicknames.size();
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        for (String nickname : nicknames) {
            players.put(nickname, new Player());
        }
        setup();
    }

    /**
     * Creates and assigns a {@code PersonalGoalCard} for each {@code Player}.
     */
    private void distributePersonalCards(){
        Gson gson = new GsonBuilder().serializeNulls()
                .setPrettyPrinting()
                .disableJdkUnsafe()
                .enableComplexMapKeySerialization()
                .create();
        List<Integer> alreadyTaken = new ArrayList<>();
        int selected = -1;
        String filename;
        PersonalGoalPattern personalGoalPattern ;
        PersonalGoalCard personalGoalCard;


        for(Player p : players.values()) {
            while (selected == -1 || alreadyTaken.contains(selected)){
                selected = random.nextInt(12);
            }

            alreadyTaken.add(selected);

            filename = "/configuration/personalGoalCards/personal_goal_pattern_" + selected + ".json";

            try (Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))) {
                 personalGoalPattern = gson.fromJson(reader,new TypeToken<PersonalGoalPattern>(){}.getType());
            } catch(IOException e){
                personalGoalPattern = null;
                System.err.println("""
                    Configuration file for personalGoalCard not found.
                    The configuration file should be in configuration/personalGoalCard
                    with name personal_goal_pattern_{selected}""");
            }

            personalGoalCard = new PersonalGoalCard(selected, personalGoalPattern);

            p.setPersonalGoalCard(personalGoalCard);
        }
    }

    /**
     * Initializes a {@code PersonalGoalCard} for each {@code Player}, the {@code BoardManager}, the
     * {@code CommonGoalCardManager}, and fills the {@code LivingRoom}.
     */
    private void setup() throws IOException {
        this.commonGoalCardManager = new CommonGoalCardManager(numberCommonGoalCards, numberPlayers);
        this.boardManager = new BoardManager(numberPlayers);
        this.distributePersonalCards();
        this.boardManager.fill();
    }

    /**
     * {@inheritDoc}
     * @param nickname The nickname of the new current player.
     */
    @Override
    public void setCurrentPlayer(String nickname){
        currentPlayer = nickname;
    }

    /**
     * {@inheritDoc}
     * @param positions {@code List} of {@code Position}s chosen by a player.
     * @return {@code true} iff the column is available, {@code false} if otherwise.
     */
    @Override
    public boolean canTakeItemTiles(List<Position> positions){
        if (!boardManager.canTakeItemTilesBoard(positions)) return false;

        boolean availableColumn = false;
        for(int i = 0; i < players.get(currentPlayer).getBookshelf().getColumns(); i++) {
            if (players.get(currentPlayer).getBookshelf().canInsert(positions.size(), i)) availableColumn = true;
        }
        return availableColumn;
    }

    /**
     * {@inheritDoc}
     * @param positions {@code List} of {@code Position}s chosen by the current player.
     * @return The {@code Item}s selected from the {@code LivingRoom}.
     */
    @Override
    public List<Item> selectItemTiles(List<Position> positions){
        List<Pair<Position, Item>> selectedItems = boardManager.selectItemTiles(positions);
        players.get(currentPlayer).takeItems(selectedItems);
        return selectedItems.stream().map(Pair::getValue).toList();
    }

    @Override
    public void resetTilesSelected() {
        List<Pair<Position, Item>> posToItems = players.get(currentPlayer).getAndRemoveItems();
        if (posToItems != null) boardManager.resetTiles(posToItems);
    }

    /**
     * {@inheritDoc}
     * @param column The column of the bookshelf.
     * @param order A permutation representing the order to insert the elements in.
     * @return {@code true} iff the move is valid.
     */
    @Override
    public boolean canInsertItemTilesInBookshelf(int column, List<Integer> order) {
        return players.get(currentPlayer).canInsertTiles(column, order);
    }

    /**
     * {@inheritDoc}
     * @param column The column to insert in.
     * @param order {@code List} representing the order of the {@code Item}s to insert in {@code Bookshelf}.
     */
    @Override
    public void insertItemTilesInBookshelf(int column, List<Integer> order) {
        players.get(currentPlayer).insertTiles(column, order);
        endTurn();
    }

    /**
     * Takes the ending token if bookshelf is full, and it is available;
     * Refills board if needed and bag is non-empty;
     * Check common goals.
     */
    private void endTurn() {
        if (players.get(currentPlayer).getBookshelf().isFull() && boardManager.isEndingToken()) {
            boardManager.takeEndingToken();
            players.get(currentPlayer).addEndingToken();
            if (endingTokenListener != null) {
                endingTokenListener.updateState(currentPlayer);
            }
        }

        boardManager.fill();

        List<ScoringToken> tokens = commonGoalCardManager.check(players.get(currentPlayer).getBookshelf(),
                players.get(currentPlayer).cannotTake());
        tokens.forEach(t -> players.get(currentPlayer).addScoringToken(t));
    }

    /**
     * {@inheritDoc}
     * @return {@code true} iff there is a player that owns the ending token.
     */
    @Override
    public boolean isEndingTokenAssigned() {
        for(Player p : players.values()) {
            if (p.firstToFinish()) return true;
        }
        return false;
    }

    /**
     * Sets the {@code BookshelfListener} for a player.
     * @param bookshelfListener The {@code BookshelfListener} of the player to set.
     */
    public void setBookshelfListener(BookshelfListener bookshelfListener) {
        Player player = players.get(bookshelfListener.getOwner());
        if (player == null) return;

        player.setBookshelvesListener(bookshelfListener);
    }

    /**
     * Sets the {@code CommonGoalCardsListener}.
     * @param commonGoalCardsListener The {@code CommonGoalCardsListener} to set.
     */
    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        if (commonGoalCardManager == null) return;
        commonGoalCardManager.setCommonGoalCardsListener(commonGoalCardsListener);
    }

    /**
     * Sets the {@code EndingTokenListener} and updates it with the current state.
     * @param endingTokenListener The {@code EndingTokenListener} to set.
     */
    public void setEndingTokenListener(EndingTokenListener endingTokenListener) {
        this.endingTokenListener = endingTokenListener;
        for (Map.Entry<String, Player> e: players.entrySet()) {
            if (e.getValue().firstToFinish()) {
                endingTokenListener.updateState(e.getKey());
                return;
            }
        }

        endingTokenListener.updateState(null);
    }

    /**
     * Sets the {@code LivingRoomListener}.
     * @param livingRoomListener The {@code LivingRoomListener} to set.
     */
    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        if (boardManager == null) return;
        boardManager.setLivingRoomListener(livingRoomListener);
    }

    /**
     * {@inheritDoc}
     * @param nickname The {@code Players}'s nickname.
     * @return The public score of the {@code Player}.
     */
    @Override
    public int getPublicScore(String nickname) {
        return this.players.get(nickname).getPublicScore();
    }

    /**
     * {@inheritDoc}
     * @param nickname The {@code Player}'s name.
     * @return The personal score of a {@code Player}.
     */
    @Override
    public int getPersonalScore(String nickname) {
        return this.players.get(nickname).getPersonalScore();
    }

    /**
     * Getter for the current player.
     * @return The current player.
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * {@inheritDoc}
     * @param nickname The nickname of the player.
     * @return The id of the {@code PersonalGoalCard} of the player with nickname {@code nickname}.
     */
    @Override
    public int getPersonalID(String nickname) {
        return players.get(nickname).getPersonalID();
    }

    /**
     * Getter for the number of players.
     * @return The number of players.
     */
    public int getNumberPlayers() {
        return numberPlayers;
    }

    //METHODS EXCLUSIVELY FOR TESTING

    /**
     * Getter for the players.
     * @return A {@code Map} of the nickname -> players.
     */
    public Map<String, Player> getPlayers() {
        return players;
    }

    /**
     * Getter for the {@code BoardManager}.
     * @return The {@code BoardManager}.
     */
    public BoardManager getBoardManager() {
        return boardManager;
    }

    /**
     * Getter for the {@code CommonGoalCardManager}.
     * @return The {@code CommonGoalCardManager}.
     */
    public CommonGoalCardManager getCommonGoalCardManager() {
        return commonGoalCardManager;
    }
}