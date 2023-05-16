package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.view.change.BookshelfListener;
import it.polimi.ingsw.view.change.ItemsChosenListener;
import it.polimi.ingsw.view.change.PersonalGoalCardListener;

import java.util.*;

/**
 * Class representing a Player.
 */

public class Player {
    private final String nickname;
    private List<Item> itemsTakenFromLivingRoom;
    private final Bookshelf bookshelf;
    private PersonalGoalCard personalGoalCard;
    private final List<ScoringToken> scoringTokens;
    private boolean endingToken;
    private final List<PersonalGoalCardListener> personalGoalCardListeners;
    private final List<BookshelfListener> bookshelfReps;
    private ItemsChosenListener itemsChosenRep;

    /**
     * Player's Constructor: it initializes scores to zero, tokens to null, and it creates a Bookshelf and a PersonalGoalCard.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.itemsTakenFromLivingRoom = null;
        this.bookshelf = new Bookshelf(6, 5);
        this.personalGoalCard = null;
        this.scoringTokens = new ArrayList<>();
        this.endingToken = false;
        this.personalGoalCardListeners = new ArrayList<>();
        this.bookshelfReps = new ArrayList<>();
    }

    /**
     * Add to {@code this} the items taken from the living room.
     *
     * @param items list of the items taken from the living room.
     */
    public void takeItems(List<Item> items) {
        itemsTakenFromLivingRoom = items;
        List<Item> copy = new ArrayList<>(items);
        if (itemsChosenRep != null) itemsChosenRep.updateState(copy);
    }

    /**
     * Setter for the personal goal card.
     *
     * @param card the card to set.
     */
    public void setPersonalGoalCard(PersonalGoalCard card) {
        this.personalGoalCard = card;
    }

    /**
     * Add a {@code ScoringToken} to {@code this} checking token's type.
     *
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
     *
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
        int count = 0;
        for(int i = bookshelf.getRows() - 1; count < permutedItems.size(); i--) {
            if (bookshelf.tileAt(i, column) == null) continue;
            count++;
            for(BookshelfListener rep : bookshelfReps) {
                rep.updateState(nickname, new Position(i, column), bookshelf.tileAt(i, column));
            }
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
     *
     * @return sum of {@code ScoringToken}s, {@code personalScore} and {@code bookshelfScore}.
     */
    public int getTotalScore() {
        return getPublicScore() + getPersonalScore();
    }

    /**
     * Get the {@code Bookshelf} of {@code this}.
     *
     * @return {@code Bookshelf} of {@code this}.
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    /**
     * Get the {@code personalScore} of {@code this}.
     *
     * @return {@code personalScore} of {@code this}.
     */
    public int getPersonalScore() {
        if (personalGoalCard == null) return 0;
        return personalGoalCard.getPersonalScore(bookshelf);
    }

    /**
     * This method tells if the {@code this} is the first to fill their {@code Bookshelf}.
     *
     * @return It returns a boolean, true iff {@code Player} has the {@code endingToken}, else false.
     */
    public boolean firstToFinish() {
        return this.endingToken;
    }

    public void setPersonalGoalCardListener(PersonalGoalCardListener listener) {
        personalGoalCardListeners.add(listener);
        if (personalGoalCard != null) listener.updateState(personalGoalCard.getId());
    }

    public void setBookshelfListener(BookshelfListener listener) {
        bookshelfReps.add(listener);
        for(int i = 0; i < bookshelf.getRows(); i++) {
            for(int j = 0; j < bookshelf.getColumns(); j++) {
                listener.updateState(nickname, new Position(i, j), bookshelf.tileAt(i, j));
            }
        }
    }

    public void setItemsChosenListener(ItemsChosenListener listener) {
        this.itemsChosenRep = listener;
    }
}
