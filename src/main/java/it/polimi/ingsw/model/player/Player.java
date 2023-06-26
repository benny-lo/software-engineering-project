package it.polimi.ingsw.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.utils.game.ScoringToken;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.controller.modelListener.BookshelfListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class representing a player of a game. It manages their {@code Bookshelf} and {@code PersonalGoalCard}.
 */
public class Player {
    private List<Item> itemsTakenFromLivingRoom;
    private final Bookshelf bookshelf;
    private PersonalGoalCard personalGoalCard;
    private final List<ScoringToken> scoringTokens;
    private boolean endingToken;
    private BookshelfListener bookshelfListener;

    /**
     * Player's Constructor: it initializes scores to zero, tokens to null, and it creates a {@code Bookshelf} and a {@code PersonalGoalCard}.
     * @throws IOException error occurred with I/O for JSON configuration files.
     */
    public Player() throws IOException {
        String filename;
        Gson gson = new GsonBuilder().serializeNulls()
                .setPrettyPrinting()
                .disableJdkUnsafe()
                .create();

        filename = "/configuration/bookshelf/bookshelf.json";

        try (Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))) {
            this.bookshelf = gson.fromJson(reader,new TypeToken<Bookshelf>(){}.getType());
        } catch(IOException e){
            System.err.println("""
                    Configuration file for bookshelf not found.
                    The configuration file should be in configuration/bookshelf""");
            throw new IOException();
        }

        this.itemsTakenFromLivingRoom = null;
        this.personalGoalCard = null;
        this.scoringTokens = new ArrayList<>();
        this.endingToken = false;
        this.bookshelfListener = null;
    }

    /**
     * Assigns to {@code this} the items taken from the living room.
     * @param items List of the items taken from the living room.
     */
    public void takeItems(List<Item> items) {
        itemsTakenFromLivingRoom = items;
    }

    /**
     * Setter for the {@code PersonalGoalCard}.
     * @param card The card to set.
     */
    public void setPersonalGoalCard(PersonalGoalCard card) {
        this.personalGoalCard = card;
    }

    /**
     * Gives a {@code ScoringToken} to {@code this}.
     * @param token {@code ScoringToken} obtained by completing a {@code CommonGoalCard}.
     */
    public void addScoringToken(ScoringToken token) {
        scoringTokens.add(token);
    }

    /**
     * Gives the ending token to {@code this}.
     */
    public void addEndingToken() {
        endingToken = true;
    }

    /**
     * Gets the types of the scoring tokens already taken.
     * @return {@code List} containing types of {@code ScoringToken}s already taken by {@code this}.
     */
    public List<Integer> cannotTake() {
        return scoringTokens.stream().map(ScoringToken::getType).toList();
    }

    /**
     * Checks if {@code this} can insert the items it owns in {@code column} and in {@code order}.
     * @param column The column of the bookshelf to insert into.
     * @param order The permutation representing the order of insertion.
     * @return {@code true} iff the move is valid.
     */
    public boolean canInsertTiles(int column, List<Integer> order) {
        if (itemsTakenFromLivingRoom == null) return false;

        if (!bookshelf.canInsert(itemsTakenFromLivingRoom.size(), column)) return false;
        if (order.size() != itemsTakenFromLivingRoom.size()) return false;

        Set<Integer> s = new HashSet<>();
        for(Integer i : order) {
            if (i < 0 || i >= order.size()) return false;
            s.add(i);
        }

        return itemsTakenFromLivingRoom.size() == s.size();
    }

    /**
     * Inserts a list of {@code Item}s in the {@code column} of the {@code Bookshelf} of {@code this}.
     * @param column The selected column index.
     * @param order  The order of insertion as a permutation.
     */
    public void insertTiles(int column, List<Integer> order) {
        List<Item> permutedItems = new ArrayList<>();
        for (Integer integer : order) {
            permutedItems.add(itemsTakenFromLivingRoom.get(integer));
        }

        bookshelf.insert(permutedItems, column);

        if (bookshelfListener == null) return;

        int top;
        for(top = bookshelf.getRows() - 1; top >= 0; top--) {
            if (bookshelf.tileAt(top, column) != null) break;
        }

        for (int i = 0; i < order.size(); i++) {
            bookshelfListener.updateState(new Position(top - i, column), bookshelf.tileAt(top - i, column));
        }

        itemsTakenFromLivingRoom = null;
    }

    /**
     * Gets the public score of {@code this}.
     * @return Sum of {@code ScoringToken}s, {@code Bookshelf} score and ending token.
     */
    public int getPublicScore() {
        return (endingToken ? 1 : 0) +
                bookshelf.getBookshelfScore() +
                scoringTokens.stream().map(ScoringToken::getScore).reduce(0, Integer::sum);
    }

    /**
     * Gets the total score of {@code this}.
     * @return Sum of {@code ScoringToken}s, {@code personalScore} and {@code bookshelfScore}.
     */
    public int getTotalScore() {
        return getPublicScore() + getPersonalScore();
    }

    /**
     * Gets the {@code Bookshelf} of {@code this}.
     * @return {@code Bookshelf} of {@code this}.
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * Gets the {@code personalScore} of {@code this}.
     * @return {@code personalScore} of {@code this}.
     */
    public int getPersonalScore() {
        if (personalGoalCard == null) return 0;
        return personalGoalCard.getPersonalScore(bookshelf);
    }

    /**
     * Checks if the {@code this} is the first to fill their {@code Bookshelf}, i.e. {@code this} has the ending token.
     * @return {@code true} iff {@code Player} has the {@code endingToken}, else false.
     */
    public boolean firstToFinish() {
        return this.endingToken;
    }

    /**
     * Sets the given bookshelfListener and updates it with current state of {@code this}' {@code Bookshelf}.
     * @param bookshelfListener {@code BookshelfListener} to set.
     */
    public void setBookshelvesListener(BookshelfListener bookshelfListener) {
        this.bookshelfListener = bookshelfListener;
        for(int i = 0; i < bookshelf.getRows(); i++) {
            for(int j = 0; j < bookshelf.getColumns(); j++) {
                bookshelfListener.updateState(new Position(i, j), bookshelf.tileAt(i, j));
            }
        }
    }

    /**
     * Getter for the id of the PersonalGoalCard of {@code this}.
     * @return The id of the PersonalGoalCard associated to the player.
     */
    public int getPersonalID() {
        return personalGoalCard.getId();
    }
}
