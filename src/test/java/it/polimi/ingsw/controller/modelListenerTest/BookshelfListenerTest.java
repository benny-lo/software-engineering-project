package it.polimi.ingsw.controller.modelListenerTest;

import it.polimi.ingsw.controller.modelListener.BookshelfListener;
import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class BookshelfListenerTest {
    /**
     * Testing for the constructor for the class
     */
    @Test
    public void constructorTest()
    {
        BookshelfListener bookshelfListener = new BookshelfListener("nick");
        assertEquals("nick", bookshelfListener.getOwner());
        assertNotNull(bookshelfListener.getBookshelf());
        assertTrue(bookshelfListener.getBookshelf().isEmpty());
    }
    /**
     * Testing for the updateState method
     */
    @Test
    public void updateStateTest()
    {
        BookshelfListener bookshelfListener = new BookshelfListener("nick");
        bookshelfListener.updateState(new Position(0,0), Item.CAT);
        bookshelfListener.updateState(new Position(1,1), Item.CUP);
        bookshelfListener.updateState(new Position(2,2), Item.BOOK);
        Map<Position,Item> map = bookshelfListener.getBookshelf();
        assertEquals(map.get(new Position(0,0)), Item.CAT);
        assertEquals(map.get(new Position(1,1)), Item.CUP);
        assertEquals(map.get(new Position(2,2)), Item.BOOK);
        assertTrue(bookshelfListener.getBookshelf().isEmpty());

    }
}
