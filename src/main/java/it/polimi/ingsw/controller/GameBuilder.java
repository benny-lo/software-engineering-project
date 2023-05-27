package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private final static int MAX_PLAYERS = 4;
    private final static int MIN_PLAYERS = 2;
    private final int numberCommonGoalCards;
    private final List<String> players;
    private final List<BookshelfListener> bookshelfListeners;
    private CommonGoalCardsListener commonGoalCardsListener;
    private EndingTokenListener endingTokenListener;
    private LivingRoomListener livingRoomListener;

    /**
     * Constructor for the class.
     * @param numberCommonGoalCards - number of common goal cards
     */
    public GameBuilder(int numberCommonGoalCards) {
        this.numberCommonGoalCards = numberCommonGoalCards;
        players = new ArrayList<>();
        bookshelfListeners = new ArrayList<>();
    }

    /**
     * This method adds a player
     * @param nickname - the nickname of the player that is added
     */
    public void addPlayer(String nickname){
        players.add(nickname);
    }

    /**
     * This method removes a player
     * @param nickname - the nickname of the player that is being removed
     */
    public void removePlayer(String nickname) {players.remove(nickname);}

    /**
     * This method adds a bookshelf listener
     * @param bookshelfListener - the bookshelf listener to be added.
     */
    public void setBookshelfListener(BookshelfListener bookshelfListener) {
        bookshelfListeners.add(bookshelfListener);
    }

    /**
     * This method removes a bookshelf listener
     * @param nickname - bookshelfListener's nickname.
     */
    public void removeBookshelfListener(String nickname) {
        bookshelfListeners.removeIf(bookshelfL -> bookshelfL.getOwner().equals(nickname));
    }

    /**
     * This method sets a CommonGoalCards listener
     * @param commonGoalCardsListener - the listener that needs to be set
     */
    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        this.commonGoalCardsListener = commonGoalCardsListener;
    }

    /**
     * This method sets an Ending Token Listener
     * @param endingTokenListener - the listener that needs to be set
     */
    public void setEndingTokenListener(EndingTokenListener endingTokenListener) {
        this.endingTokenListener = endingTokenListener;
    }

    /**
     * This method sets a living room listener
     * @param livingRoomListener - the living room listener that needs to be set.
     */
    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        this.livingRoomListener = livingRoomListener;
    }

    /**
     * Getter for the number of players
     * @return - the number of players.
     */
    public int getCurrentPlayers() {
        return players.size();
    }

    /**
     * This method starts the game, setting all the new listeners.
     * @return - the GameInterface item.
     */
    public GameInterface startGame() {
        if (bookshelfListeners.contains(null) ||
        commonGoalCardsListener == null ||
        endingTokenListener == null ||
        livingRoomListener == null) return null;

        if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) return null;

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


    public List<String> getPlayers() {
        return players;
    }

    public List<BookshelfListener> getBookshelfListeners() {
        return bookshelfListeners;
    }

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
