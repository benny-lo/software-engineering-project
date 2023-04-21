package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.board.BoardManager;
import it.polimi.ingsw.model.commonGoalCard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

    /**
     * Game's Constructor: it initializes {@code Game}.
     * @param numberCommonGoalCards It can be initialised to '1' or '2'.
     */
    public Game(int numberCommonGoalCards) {
        this.numberPlayers = 0;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        this.boardManager = null;
        this.commonGoalCardManager = null;
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
        FileReader reader;
        PersonalGoalPattern personalGoalPattern ;
        PersonalGoalCard personalGoalCard;


        for(String name : players.keySet()){
            while (selected == -1 || alreadyTaken.contains(selected)){
                selected = random.nextInt(12);
            }

            alreadyTaken.add(selected);

            filename = "src/main/resources/configuration/personalGoalCards/personal_goal_card_" + selected + ".json";

            try {
                reader = new FileReader(filename);
                 personalGoalPattern = gson.fromJson(reader,new TypeToken<PersonalGoalPattern>(){}.getType());
            } catch(FileNotFoundException e){
                personalGoalPattern = null;
                System.err.println("""
                    Configuration file for personalGoalCard not found.
                    The configuration file should be in configuration/personalGoalCard
                    with name personal_goal_card_{selected}""");
            }

            personalGoalCard = new PersonalGoalCard(personalGoalPattern);

            players.get(name).setPersonalGoalCard(personalGoalCard);
        }
    }

    @Override
    public void addPlayer(String nickname){
        this.players.put(nickname, new Player());
        this.numberPlayers += 1;
    }

    @Override
    public void setup(){
        if (numberPlayers < 2 || numberPlayers > 4)
            return;
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
        }

        boardManager.fill();

        List<ScoringToken> tokens = commonGoalCardManager.check(players.get(currentPlayer).getBookshelf(),
                players.get(currentPlayer).cannotTake());
        tokens.forEach((t) -> players.get(currentPlayer).addScoringToken(t));
    }

    @Override
    public String getWinner() {
        boolean nobodyFinished = true;
        for(String s : players.keySet()) {
            if (players.get(s).firstToFinish()) nobodyFinished = false;
        }
        if (!nobodyFinished) return null;

        int max = -1;
        String winner = null;

        for (String s: players.keySet()){
            if (players.get(s).getTotalScore() > max){
                max = players.get(s).getTotalScore();
            }
        }

        for (String s: players.keySet()){
            if (players.get(s).getTotalScore() == max && players.get(s).firstToFinish())
                return s;
            else if (players.get(s).getTotalScore() == max) {
                winner = s;
            }
        }
        return winner;
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
}