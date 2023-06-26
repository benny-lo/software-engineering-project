package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Class implementing the Builder pattern. It builds a {@code GameInterface}.
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
     * Constructor for the class. It only sets the number of {@code CommonGoalCard}s.
     * @param numberCommonGoalCards The number of common goal cards.
     */
    public GameBuilder(int numberCommonGoalCards) {
        this.numberCommonGoalCards = numberCommonGoalCards;
        players = new ArrayList<>();
        bookshelfListeners = new ArrayList<>();
    }

    /**
     * Adds a player.
     * @param nickname The nickname of the player to add.
     */
    public void addPlayer(String nickname){
        players.add(nickname);
    }

    /**
     * Removes a player.
     * @param nickname The nickname of the player to remove.
     */
    public void removePlayer(String nickname) {players.remove(nickname);}

    /**
     * Adds a {@code BookshelfListener}.
     * @param bookshelfListener The bookshelf listener to add.
     */
    public void setBookshelfListener(BookshelfListener bookshelfListener) {
        bookshelfListeners.add(bookshelfListener);
    }

    /**
     * Removes a {@code BookshelfListener}.
     * @param nickname Owner of the listener to remove.
     */
    public void removeBookshelfListener(String nickname) {
        bookshelfListeners.removeIf(bookshelfL -> bookshelfL.getOwner().equals(nickname));
    }

    /**
     * Setter for {@code CommonGoalCardsListener}.
     * @param commonGoalCardsListener The listener to set.
     */
    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        this.commonGoalCardsListener = commonGoalCardsListener;
    }

    /**
     * Setter for {@code EndingTokenListener}.
     * @param endingTokenListener The listener to set.
     */
    public void setEndingTokenListener(EndingTokenListener endingTokenListener) {
        this.endingTokenListener = endingTokenListener;
    }

    /**
     * Setter for {@code LivingRoomListener}.
     * @param livingRoomListener The listener to set.
     */
    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        this.livingRoomListener = livingRoomListener;
    }

    /**
     * Creates a {@code Game} object using the provided players' nicknames and listeners.
     * @return The {@code Game} object created, hidden as a {@code GameInterface}, if {@code this} has all
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

        for(String nickname : players) {
            List<String> tmp = bookshelfListeners.stream().
                    map(BookshelfListener::getOwner).
                    filter(nickname::equals).toList();
            if (tmp.size() != 1) return null;
        }

        Game game;
        try {
            game = new Game(players, numberCommonGoalCards);
        } catch (IOException e) {
            return null;
        }

        for(BookshelfListener bookshelfListener : bookshelfListeners) {
            game.setBookshelfListener(bookshelfListener);
        }
        game.setCommonGoalCardsListener(commonGoalCardsListener);
        game.setEndingTokenListener(endingTokenListener);
        game.setLivingRoomListener(livingRoomListener);

        return game;
    }
}
