package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.modelListener.BookshelfListener;
import it.polimi.ingsw.controller.modelListener.CommonGoalCardsListener;
import it.polimi.ingsw.controller.modelListener.EndingTokenListener;
import it.polimi.ingsw.controller.modelListener.LivingRoomListener;
import it.polimi.ingsw.model.GameInterface;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameBuilderTest {

    /**
     * Testing for the class constructor
     */
    @Test
    public void testConstructor()
    {
        GameBuilder gameBuilder = new GameBuilder(2);
        assertEquals(gameBuilder.getNumberCommonGoalCards(),2);
        assertNotNull(gameBuilder.getPlayers());
        assertNotNull(gameBuilder.getBookshelfListeners());
    }
    /**
     * Testing for adding and removing players
     */
    @Test
    public void testPlayer()
    {
        GameBuilder gameBuilder = new GameBuilder(2);
        gameBuilder.addPlayer("nick");
        gameBuilder.addPlayer("rick");
        gameBuilder.removePlayer("nick");
        gameBuilder.removePlayer("john");
        gameBuilder.addPlayer("john");
        List<String> list = gameBuilder.getPlayers();
        assertEquals(list.get(0),"rick");
        assertEquals(list.get(1),"john");

    }

    /**
     * Testing for the listeners methods. Firstly we test if adding and removing a bookshelf listener works as intended.
     * Then we add and test if adding a common goal card listener, an ending token listener and living room listener works
     */
    @Test
    public void testListeners()
    {
        GameBuilder gameBuilder = new GameBuilder(2);
        gameBuilder.addPlayer("rick");
        gameBuilder.addPlayer("nick");
        gameBuilder.setBookshelfListener(new BookshelfListener(gameBuilder.getPlayers().get(0)));
        gameBuilder.removeBookshelfListener(gameBuilder.getPlayers().get(0));
        gameBuilder.setBookshelfListener(new BookshelfListener(gameBuilder.getPlayers().get(1)));
        gameBuilder.setCommonGoalCardsListener(new CommonGoalCardsListener());
        gameBuilder.setEndingTokenListener(new EndingTokenListener());
        gameBuilder.setLivingRoomListener(new LivingRoomListener());
        assertNotNull(gameBuilder.getCommonGoalCardsListener());
        assertNotNull(gameBuilder.getEndingTokenListener());
        assertNotNull(gameBuilder.getLivingRoomListener());
        assertEquals(gameBuilder.getBookshelfListenersOwners().get(0),gameBuilder.getPlayers().get(1));

    }
    /**
     * Testing for the getter for the number of current players
     */
    @Test
    public void testGetCurrentPlayers()
    {
        GameBuilder gameBuilder = new GameBuilder(2);
        gameBuilder.addPlayer("nick");
        gameBuilder.addPlayer("rick");
        gameBuilder.addPlayer("john");
        int num = gameBuilder.getCurrentPlayers();
        assertEquals(3,num);
        gameBuilder.removePlayer("john");
        num = gameBuilder.getCurrentPlayers();
        assertEquals(2,num);
    }
    /**
     * Testing for the startGame method
     */
    @Test
    public void testStartGame()
    {
        GameBuilder gameBuilder = new GameBuilder(2);
        gameBuilder.addPlayer("nick");
        gameBuilder.addPlayer("rick");
        setup(gameBuilder);
        GameInterface game = gameBuilder.startGame();
        assertNotNull(game);
    }
    //FOR TESTING
    public void setup(GameBuilder gameBuilder)
    {
        for(String nickname : gameBuilder.getPlayers())
        {
            gameBuilder.setBookshelfListener(new BookshelfListener(nickname));
        }
        gameBuilder.setCommonGoalCardsListener(new CommonGoalCardsListener());
        gameBuilder.setEndingTokenListener(new EndingTokenListener());
        gameBuilder.setLivingRoomListener(new LivingRoomListener());

    }


}
