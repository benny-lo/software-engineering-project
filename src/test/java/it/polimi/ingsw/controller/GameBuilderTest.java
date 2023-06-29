package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.modelListener.BookshelfListener;
import it.polimi.ingsw.controller.modelListener.CommonGoalCardsListener;
import it.polimi.ingsw.controller.modelListener.EndingTokenListener;
import it.polimi.ingsw.controller.modelListener.LivingRoomListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class testing {@code GameBuilder}.
 */
public class GameBuilderTest {
    /**
     * Testing {@code GameBuilder} with no listener set.
     */
    @Test
    public void testNoListenerSet() {
        GameBuilder gameBuilder = new GameBuilder(2);
        gameBuilder.addPlayer("nick");
        gameBuilder.addPlayer("rick");

        assertNull(gameBuilder.startGame());
    }

    /**
     * Testing {@code GameBuilder} with only the listeners set.
     */
    @Test
    public void testOnlyListenersSet() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, new ArrayList<>());

        assertNull(gameBuilder.startGame());
    }

    /**
     * Testing {@code GameBuilder} with players and listeners set but the number of players is incorrect.
     */
    @Test
    public void testGetCurrentPlayers() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, List.of("nick", "rick"));
        gameBuilder.addPlayer("john");

        assertNull(gameBuilder.startGame());
    }

    /**
     * Testing {@code GameBuilder} with two players with same nickname.
     */
    @Test
    public void testSameNickname() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, List.of("nick", "nick"));

        assertNull(gameBuilder.startGame());
    }

    /**
     * Test creating a {@code Game} with 5 players.
     */
    @Test
    public void testTooManyPlayers() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, List.of("alice", "bob", "nick", "rick", "joe"));

        assertNull(gameBuilder.startGame());
    }

    /**
     * Test with {@code BookshelfListener} not backed by any of the players.
     */
    @Test
    public void testBookshelfListenerNotMatching() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, List.of("alice"));
        gameBuilder.addPlayer("bob");
        gameBuilder.setBookshelfListener(new BookshelfListener("nick"));

        assertNull(gameBuilder.startGame());
    }

    /**
     * Test creating a {@code Game} with repeated {@code BookshelfListener}s for one player.
     */
    @Test
    public void testMoreBookshelfListeners() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, List.of("alice", "bob"));
        gameBuilder.setBookshelfListener(new BookshelfListener("bob"));

        assertNull(gameBuilder.startGame());
    }

    /**
     * Test creating a {@code Game} with two players with same nickname.
     */
    @Test
    public void testRepeatedPlayers() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, List.of("alice", "alice"));

        assertNull(gameBuilder.startGame());
    }

    /**
     * Test creating a {@code Game} with two players is correctly created.
     */
    @Test
    public void testCorrectGameCreation() {
        GameBuilder gameBuilder = new GameBuilder(2);
        setup(gameBuilder, List.of("alice", "bob"));

        assertNotNull(gameBuilder.startGame());
    }

    private void setup(GameBuilder gameBuilder, List<String> nicknames) {
        for(String nickname : nicknames) {
            gameBuilder.addPlayer(nickname);
            gameBuilder.setBookshelfListener(new BookshelfListener(nickname));
        }
        gameBuilder.setCommonGoalCardsListener(new CommonGoalCardsListener());
        gameBuilder.setEndingTokenListener(new EndingTokenListener());
        gameBuilder.setLivingRoomListener(new LivingRoomListener());

    }


}
