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

    public GameBuilder(int numberCommonGoalCards) {
        this.numberCommonGoalCards = numberCommonGoalCards;
        players = new ArrayList<>();
        bookshelfListeners = new ArrayList<>();
    }

    public void addPlayer(String nickname){
        players.add(nickname);
    }

    public void setBookshelfListener(BookshelfListener bookshelfListener) {
        bookshelfListeners.add(bookshelfListener);
    }

    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        this.commonGoalCardsListener = commonGoalCardsListener;
    }

    public void setEndingTokenListener(EndingTokenListener endingTokenListener) {
        this.endingTokenListener = endingTokenListener;
    }

    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        this.livingRoomListener = livingRoomListener;
    }

    public int getCurrentPlayers() {
        return players.size();
    }

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
}
