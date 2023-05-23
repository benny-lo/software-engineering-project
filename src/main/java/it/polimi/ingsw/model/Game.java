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
     */
    public Game(List<String> nicknames, int numberCommonGoalCards) {
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
    private void setup(){
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
    public List<Item> selectItemTiles(List<Position> positions){
        List<Item> selectedItems = boardManager.selectItemTiles(positions);
        players.get(currentPlayer).takeItems(selectedItems);
        return new ArrayList<>(selectedItems);
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
            if (endingTokenListener != null) {
                endingTokenListener.updateState(currentPlayer);
            }
        }

        boardManager.fill();

        List<ScoringToken> tokens = commonGoalCardManager.check(players.get(currentPlayer).getBookshelf(),
                players.get(currentPlayer).cannotTake());
        tokens.forEach((t) -> players.get(currentPlayer).addScoringToken(t));
    }

    @Override
    public boolean IsEndingTokenAssigned() {
        for(String nickname : players.keySet()) {
            if (players.get(nickname).firstToFinish()) return true;
        }
        return false;
    }

    public void setBookshelfListener(BookshelfListener bookshelfListener) {
        Player player = players.get(bookshelfListener.getOwner());
        if (player == null) return;

        player.setBookshelvesListener(bookshelfListener);
    }

    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        if (commonGoalCardManager == null) return;
        commonGoalCardManager.setCommonGoalCardsRep(commonGoalCardsListener);
    }

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

    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        if (boardManager == null) return;
        boardManager.setLivingRoomListener(livingRoomListener);
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
    public int getPersonalID(String nickname) {
        return players.get(nickname).getPersonalID();
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
}