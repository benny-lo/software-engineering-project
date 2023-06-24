package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.board.BoardManager;
import it.polimi.ingsw.model.commonGoalCard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.utils.game.ScoringToken;

import java.io.*;
import java.util.*;

/**
 * Class representing the Game.
 */
public class Game implements GameInterface {
    private final int numberPlayers;
    private final int numberCommonGoalCards;
    private String currentPlayer;
    private final Map<String, Player> players;
    private BoardManager boardManager;
    private CommonGoalCardManager commonGoalCardManager;
    private EndingTokenListener endingTokenListener;

    /**
     * Game's Constructor: it initializes {@code Game}.
     * @param nicknames list containing the nicknames of the players.
     * @param numberCommonGoalCards It can be initialised to '1' or '2'.
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
     * This private method creates a {@code PersonalGoalCard} for each {@code Player}.
     */
    private void distributePersonalCards(){
        Random random = new Random();
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


        for(String name : players.keySet()){
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

            players.get(name).setPersonalGoalCard(personalGoalCard);
        }
    }

    /**
     * This method  initializes a {@code PersonalGoalCard} for each {@code Player}, the {@code BoardManager}, the
     * {@code CommonGoalCardManager}, and fills the {@code LivingRoom}.
     */
    private void setup() throws IOException {
        this.commonGoalCardManager = new CommonGoalCardManager(numberCommonGoalCards, numberPlayers);
        this.boardManager = new BoardManager(numberPlayers);
        this.distributePersonalCards();
        this.boardManager.fill();
    }

    /**
     * This method sets the currentPlayer, given the nickname.
     * @param nickname {@code nickname} is the {@code currentPlayer}'s name.
     */
    @Override
    public void setCurrentPlayer(String nickname){
        currentPlayer = nickname;
    }

    /**
     * This method checks if the positions in the list are available to be taken by a player.
     * @param positions {@code positions} is a list of {@code Position}s chosen by a player.
     * @return True if the column is available, false if otherwise.
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
     * This method selects the Items present at the positions saved in the list.
     * @param positions {@code positions} is a list of {@code Position}s chosen by a player.
     * @return Returns the list of selected Items.
     */
    @Override
    public List<Item> selectItemTiles(List<Position> positions){
        List<Item> selectedItems = boardManager.selectItemTiles(positions);
        players.get(currentPlayer).takeItems(selectedItems);
        return new ArrayList<>(selectedItems);
    }

    /**
     * This method checks if the player can insert certain items , in a certain column
     * @param column The column of the bookshelf.
     * @param order A permutation representing the order to insert the elements in.
     * @return True if the player can, false if the player can't.
     */
    @Override
    public boolean canInsertItemTilesInBookshelf(int column, List<Integer> order) {
        return players.get(currentPlayer).canInsertTiles(column, order);
    }

    /**
     * This method inserts the items in the list in the bookshelf, and ends the player's turn.
     * @param column The column to insert in.
     * @param order The list representing a permutation of the items.
     */
    @Override
    public void insertItemTilesInBookshelf(int column, List<Integer> order) {
        players.get(currentPlayer).insertTiles(column, order);
        endTurn();
    }

    /**
     * Take ending token if bookshelf is full, and it is available;
     * Refill board if needed and bag is non-empty;
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
        tokens.forEach((t) -> players.get(currentPlayer).addScoringToken(t));
    }

    /**
     * This method checks if the EndingToken has been assigned to any player.
     * @return True if it has been assigned, false otherwise.
     */
    @Override
    public boolean IsEndingTokenAssigned() {
        for(String nickname : players.keySet()) {
            if (players.get(nickname).firstToFinish()) return true;
        }
        return false;
    }

    /**
     * This method sets the Bookshelf listener of the player, in case it is not set.
     * @param bookshelfListener The bookshelf listener of the player.
     */
    public void setBookshelfListener(BookshelfListener bookshelfListener) {
        Player player = players.get(bookshelfListener.getOwner());
        if (player == null) return;

        player.setBookshelvesListener(bookshelfListener);
    }

    /**
     * This method sets the CommonGoalCards listener, in case it is not set.
     * @param commonGoalCardsListener The common goal cards listener
     */
    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        if (commonGoalCardManager == null) return;
        commonGoalCardManager.setCommonGoalCardsRep(commonGoalCardsListener);
    }

    /**
     * This method sets the EndingToken listener, updating the state of it for every player.
     * @param endingTokenListener The ending token listener
     */
    public void setEndingTokenListener(EndingTokenListener endingTokenListener) {
        this.endingTokenListener = endingTokenListener;
        for (String nickname : players.keySet()) {
            if (players.get(nickname).firstToFinish()) {
                endingTokenListener.updateState(nickname);
                return;
            }
        }

        endingTokenListener.updateState(null);
    }

    /**
     * This method sets the LivingRoom listener, in case it is not set.
     * @param livingRoomListener The living room listener.
     */
    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        if (boardManager == null) return;
        boardManager.setLivingRoomListener(livingRoomListener);
    }

    /**
     * Getter for the PublicScore of a Player
     * @param nickname It's the {@code Players}'s name.
     * @return The public score of a player.
     */
    @Override
    public int getPublicScore(String nickname) {
        return this.players.get(nickname).getPublicScore();
    }

    /**
     * Getter for the PersonalScore of a Player.
     * @param nickname the {@code Player}'s name.
     * @return The private score of a player.
     */
    @Override
    public int getPersonalScore(String nickname) {
        return this.players.get(nickname).getPersonalScore();
    }

    /**
     * Getter for the CurrentPlayer
     * @return The current player.
     */
    @Override
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Getter for the PersonalID of a player, using the nickname.
     * @param nickname the nickname of the player.
     * @return The personal ID of a player.
     */
    @Override
    public int getPersonalID(String nickname) {
        return players.get(nickname).getPersonalID();
    }

    /**
     * Getter for the number of players.
     * @return The number of players.
     */
    @Override
    public int getNumberPlayers() {
        return numberPlayers;
    }

    //METHODS EXCLUSIVELY FOR TESTING

    /**
     * Getter for the players
     * @return A map of the players.
     */
    public Map<String, Player> getPlayers() {
        return players;
    }

    /**
     * Getter for the boardManager
     * @return The board manager
     */
    public BoardManager getBoardManager() {
        return boardManager;
    }

    /**
     * Getter for the CommonGoalCardManager
     * @return The common goal card manager.
     */
    public CommonGoalCardManager getCommonGoalCardManager() {
        return commonGoalCardManager;
    }
}