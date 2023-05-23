package it.polimi.ingsw.model.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.controller.modelListener.BookshelfListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class representing a Player.
 */

public class Player {
    private List<Item> itemsTakenFromLivingRoom;
    private final Bookshelf bookshelf;
    private PersonalGoalCard personalGoalCard;
    private final List<ScoringToken> scoringTokens;
    private boolean endingToken;
    private BookshelfListener bookshelfListener;

    /**
     * Player's Constructor: it initializes scores to zero, tokens to null, and it creates a Bookshelf and a PersonalGoalCard.
     */
    public Player() {
        String filename;
        Bookshelf b;
        Gson gson = new GsonBuilder().serializeNulls()
                .setPrettyPrinting()
                .disableJdkUnsafe()
                .create();

        filename = "/configuration/bookshelf/bookshelf.json";

        try (Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))) {
            b = gson.fromJson(reader,new TypeToken<Bookshelf>(){}.getType());
        } catch(IOException e){
            b = null;
            System.err.println("""
                    Configuration file for bookshelf not found.
                    The configuration file should be in configuration/bookshelf""");
        }

        this.itemsTakenFromLivingRoom = null;
        this.bookshelf = b;
        this.personalGoalCard = null;
        this.scoringTokens = new ArrayList<>();
        this.endingToken = false;
    }

    /**
     * Add to {@code this} the items taken from the living room.
     * @param items list of the items taken from the living room.
     */
    public void takeItems(List<Item> items) {
        itemsTakenFromLivingRoom = items;
    }

    /**
     * Setter for the personal goal card.
     * @param card the card to set.
     */
    public void setPersonalGoalCard(PersonalGoalCard card) {
        this.personalGoalCard = card;
    }

    /**
     * Add a {@code ScoringToken} to {@code this} checking token's type.
     * @param token {@code ScoringToken} obtained completing a {@code CommonGoalCard}.
     */
    public void addScoringToken(ScoringToken token) {
        scoringTokens.add(token);
    }

    /**
     * Add the ending token to {@code this}.
     */
    public void addEndingToken() {
        endingToken = true;
    }

    /**
     * Get the types of the scoring tokens already taken.
     * @return list containing types of scoring tokens taken by {@code this}.
     */
    public List<Integer> cannotTake() {
        return scoringTokens.stream().map(ScoringToken::getType).toList();
    }

    /**
     * Check if {@code this} can insert the items it owns in {@code column} and in {@code order}.
     * @param column the column of the bookshelf to insert into.
     * @param order the permutation representing the order.
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
     * Insert a list of {@code Item}s in the {@code Bookshelf} of {@code this} in the {@code column}.
     *
     * @param column a {@code column} selected by the Player.
     * @param order  the order to give to the items.
     */
    public void insertTiles(int column, List<Integer> order) {
        List<Item> permutedItems = new ArrayList<>();
        for (Integer integer : order) {
            permutedItems.add(itemsTakenFromLivingRoom.get(integer));
        }

        bookshelf.insert(permutedItems, column);

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
     * Get the public score of {@code this}.
     *
     * @return sum of {@code ScoringToken}s.
     */
    public int getPublicScore() {
        return (endingToken ? 1 : 0) +
                bookshelf.getBookshelfScore() +
                scoringTokens.stream().map(ScoringToken::getScore).reduce(0, Integer::sum);
    }

    /**
     * Get the total score of {@code this}.
     * @return sum of {@code ScoringToken}s, {@code personalScore} and {@code bookshelfScore}.
     */
    public int getTotalScore() {
        return getPublicScore() + getPersonalScore();
    }

    /**
     * Get the {@code Bookshelf} of {@code this}.
     * @return {@code Bookshelf} of {@code this}.
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * Get the {@code personalScore} of {@code this}.
     * @return {@code personalScore} of {@code this}.
     */
    public int getPersonalScore() {
        if (personalGoalCard == null) return 0;
        return personalGoalCard.getPersonalScore(bookshelf);
    }

    /**
     * This method tells if the {@code this} is the first to fill their {@code Bookshelf}.
     * @return It returns a boolean, true iff {@code Player} has the {@code endingToken}, else false.
     */
    public boolean firstToFinish() {
        return this.endingToken;
    }

    /**
     * This method sets the given bookshelfListener, by updating the state of every tile in the bookshelf.
     * @param bookshelfListener - bookshelfListener that needs to be set
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
     * Getter for the id of the PersonalGoalCard of {@code this}
     * @return - the id of the PersonalGoalCard associated to the player
     */
    public int getPersonalID() {
        return personalGoalCard.getId();
    }
}
