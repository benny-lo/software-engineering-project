package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Class implementing the Builder pattern to construct a {@code Game} instance
 * returned as a {@code GameInterface}.
 */
public class GameBuilder {
    /**
     * Maximum number of players allowed.
     */
    private final static int MAX_PLAYERS = 4;

    /**
     * Minimum number of players allowed.
     */
    private final static int MIN_PLAYERS = 2;

    /**
     * Number of common goal cards of the {@code Game} to be constructed.
     */
    private final int numberCommonGoalCards;

    /**
     * List containing the nicknames of players that want to join the game.
     */
    private final List<String> players;

    /**
     * List of listeners of the bookshelf, one per logged player.
     */
    private final List<BookshelfListener> bookshelfListeners;

    /**
     * Listener for common goal cards.
     */
    private CommonGoalCardsListener commonGoalCardsListener;

    /**
     * Listener for the ending token.
     */
    private EndingTokenListener endingTokenListener;

    /**
     * Listener for the living room.
     */
    private LivingRoomListener livingRoomListener;

    /**
     * Constructor for the class. It only sets the number of common goal cards.
     * @param numberCommonGoalCards number of common goal cards.
     */
    public GameBuilder(int numberCommonGoalCards) {
        this.numberCommonGoalCards = numberCommonGoalCards;
        players = new ArrayList<>();
        bookshelfListeners = new ArrayList<>();
    }

    /**
     * Adds a player. It assumes the nickname is not already logged in.
     * @param nickname the nickname of the player that is added.
     */
    public void addPlayer(String nickname){
        players.add(nickname);
    }

    /**
     * Removes a player.
     * @param nickname the nickname of the player that to remove.
     */
    public void removePlayer(String nickname) {players.remove(nickname);}

    /**
     * Adds a bookshelf listener.
     * @param bookshelfListener the bookshelf listener to add.
     */
    public void setBookshelfListener(BookshelfListener bookshelfListener) {
        bookshelfListeners.add(bookshelfListener);
    }

    /**
     * Removes a bookshelf listener.
     * @param nickname owner of the listener to remove.
     */
    public void removeBookshelfListener(String nickname) {
        bookshelfListeners.removeIf(bookshelfL -> bookshelfL.getOwner().equals(nickname));
    }

    /**
     * Setter for {@code CommonGoalCardsListener}.
     * @param commonGoalCardsListener the listener to set.
     */
    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        this.commonGoalCardsListener = commonGoalCardsListener;
    }

    /**
     * Setter for {@code EndingTokenListener}.
     * @param endingTokenListener the listener to set.
     */
    public void setEndingTokenListener(EndingTokenListener endingTokenListener) {
        this.endingTokenListener = endingTokenListener;
    }

    /**
     * Setter for {@code LivingRoomListener}.
     * @param livingRoomListener the listener to set.
     */
    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        this.livingRoomListener = livingRoomListener;
    }

    /**
     * Getter for the number of players.
     * @return the number of players.
     */
    public int getCurrentPlayers() {
        return players.size();
    }

    /**
     * Creates a {@code Game} object and returns as a {@code GameInterface}.
     * @return the {@code Game} object created, hidden as a {@code GameInterface} if {@code this} has all
     * listeners and the list of players set in a consistent way, else {@code null}.
     */
    public GameInterface startGame() {
        if (bookshelfListeners.contains(null) ||
        commonGoalCardsListener == null ||
        endingTokenListener == null ||
        livingRoomListener == null) return null;

        if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) return null;

        if ((new HashSet<>(players)).size() != players.size()) return null;

        for (BookshelfListener listener : bookshelfListeners) {
            if (!players.contains(listener.getOwner())) return null;
        }

        Game game = new Game(players, numberCommonGoalCards);
        for(BookshelfListener bookshelfListener : bookshelfListeners) {
            game.setBookshelfListener(bookshelfListener);
        }
        game.setCommonGoalCardsListener(commonGoalCardsListener);
        game.setEndingTokenListener(endingTokenListener);
        game.setLivingRoomListener(livingRoomListener);

        return game;
    }

    //METHODS EXCLUSIVELY FOR TESTING

    /**
     * Getter for the players
     * @return - a list formed by string of players
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Getter for the bookshelves listeners
     * @return - a list formed by the Bookshelf listeners
     */
    public List<BookshelfListener> getBookshelfListeners() {
        return bookshelfListeners;
    }

    /**
     * Getter for the owners of the bookshelves
     * @return - a list of the owners of the bookshelves.
     */
    public List<String> getBookshelfListenersOwners(){
        return getBookshelfListeners().stream().map(BookshelfListener::getOwner).toList();
    }
    /**
     * Getter for the number of common goal cards
     * @return - number of CommonGoalCards
     */
    public int getNumberCommonGoalCards()
    {
        return this.numberCommonGoalCards;
    }
    /**
     * Getter for Common Goal Cards Listener
     * @return - the common Goal Cards Listener
     */
    public CommonGoalCardsListener getCommonGoalCardsListener()
    {
        return this.commonGoalCardsListener;
    }
    /**
     * Getter for Ending Token Listener
     * @return - the Ending Token Listener
     */
    public EndingTokenListener getEndingTokenListener()
    {
        return this.endingTokenListener;
    }
    /**
     * Getter for Living Room Listener
     * @return - the Living Room Listener
     */
    public LivingRoomListener getLivingRoomListener()
    {
        return this.livingRoomListener;
    }

}
