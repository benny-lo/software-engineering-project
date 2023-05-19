package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.board.BoardManager;
import it.polimi.ingsw.model.commonGoalCard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import it.polimi.ingsw.view.modelListener.*;

import java.io.*;
import java.util.*;

/**
 * Class representing the Game.
 */
public class Game implements GameInterface {
    private int numberPlayers;
    private final int numberCommonGoalCards;
    private String currentPlayer;
    private final Map<String, Player> players;
    private BoardManager boardManager;
    private CommonGoalCardManager commonGoalCardManager;
    private final List<EndingTokenListener> endingTokenListeners;
    private final List<ScoreListener> scoreListeners;

    /**
     * Game's Constructor: it initializes {@code Game}.
     * @param numberCommonGoalCards It can be initialised to '1' or '2'.
     */
    public Game(int numberCommonGoalCards, int numberPlayers, Map<String, Player> players) {
        this.numberPlayers = numberPlayers;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.currentPlayer = null;
        this.players = players;
        this.boardManager = null;
        this.commonGoalCardManager = null;
        this.endingTokenListeners = new ArrayList<>();
        this.scoreListeners = new ArrayList<>();
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

            filename = "/configuration/personalGoalCards/personal_goal_card_" + selected + ".json";

            try (Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))) {
                 personalGoalPattern = gson.fromJson(reader,new TypeToken<PersonalGoalPattern>(){}.getType());
            } catch(IOException e){
                personalGoalPattern = null;
                System.err.println("""
                    Configuration file for personalGoalCard not found.
                    The configuration file should be in configuration/personalGoalCard
                    with name personal_goal_card_{selected}""");
            }

            personalGoalCard = new PersonalGoalCard(selected, personalGoalPattern);

            players.get(name).setPersonalGoalCard(personalGoalCard);
        }
    }

    @Override
    public void setup(){
        this.commonGoalCardManager = new CommonGoalCardManager(numberCommonGoalCards, numberPlayers);
        this.boardManager = new BoardManager(numberPlayers);
        this.distributePersonalCards();
        this.boardManager.fill();
    }

    @Override
    public void setCurrentPlayer(String nickname){
        currentPlayer = nickname;
    }

    @Override
    public boolean canTakeItemTiles(List<Position> positions){
        if (!boardManager.canTakeItemTilesBoard(positions)) return false;

        boolean availableColumn = false;
        for(int i = 0; i < players.get(currentPlayer).getBookshelf().getColumns(); i++) {
            if (players.get(currentPlayer).getBookshelf().canInsert(positions.size(), i)) availableColumn = true;
        }
        return availableColumn;
    }

    @Override
    public void selectItemTiles(List<Position> positions){
        List<Item> selectedItems = boardManager.selectItemTiles(positions);
        players.get(currentPlayer).takeItems(selectedItems);
    }

    @Override
    public boolean canInsertItemTilesInBookshelf(int column, List<Integer> order) {
        return players.get(currentPlayer).canInsertTiles(column, order);
    }

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
            for(EndingTokenListener rep : endingTokenListeners) {
                rep.updateState(currentPlayer);
            }
        }

        boardManager.fill();

        List<ScoringToken> tokens = commonGoalCardManager.check(players.get(currentPlayer).getBookshelf(),
                players.get(currentPlayer).cannotTake());
        tokens.forEach((t) -> players.get(currentPlayer).addScoringToken(t));

        for(ScoreListener rep : scoreListeners) {
            for(String nickname : players.keySet()) {
                rep.updateState(nickname, players.get(nickname).getTotalScore());
            }
        }
    }

    @Override
    public boolean IsEndingTokenAssigned() {
        for(String nickname : players.keySet()) {
            if (players.get(nickname).firstToFinish()) return true;
        }
        return false;
    }

    @Override
    public void setEndingTokenListener(EndingTokenListener listener) {
        endingTokenListeners.add(listener);
    }

    @Override
    public void setScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
        for(String nickname : players.keySet()) {
            listener.updateState(nickname, players.get(nickname).getTotalScore());
        }
    }

    @Override
    public void setBookshelfListener(BookshelfListener listener) {
        for(String nickname : players.keySet()) {
            players.get(nickname).setBookshelfListener(listener);
        }
    }

    @Override
    public void setCommonGoalCardsListener(CommonGoalCardsListener listener) {
        if (commonGoalCardManager == null) return;
        commonGoalCardManager.setCommonGoalCardsRep(listener);
    }

    @Override
    public void setLivingRoomListener(LivingRoomListener listener) {
        if (boardManager == null) return;
        boardManager.setLivingRoomListener(listener);
    }

    @Override
    public void setPersonalGoalCardListener(PersonalGoalCardListener listener) {
        players.get(listener.getOwner()).setPersonalGoalCardListener(listener);
    }

    @Override
    public void setItemsChosenListener(ItemsChosenListener listener) {
        players.get(listener.getOwner()).setItemsChosenListener(listener);
    }

    @Override
    public int getPublicScore(String nickname) {
        return this.players.get(nickname).getPublicScore();
    }

    @Override
    public int getPersonalScore(String nickname) {
        return this.players.get(nickname).getPersonalScore();
    }

    @Override
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public int getNumberPlayers() {
        return numberPlayers;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public CommonGoalCardManager getCommonGoalCardManager() {
        return commonGoalCardManager;
    }

    // EXCLUSIVELY FOR TESTING
    public void addPlayer(String nickname){
        this.players.put(nickname, new Player(nickname));
        numberPlayers++;
    }
}