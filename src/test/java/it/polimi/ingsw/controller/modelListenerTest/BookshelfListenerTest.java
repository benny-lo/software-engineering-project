package it.polimi.ingsw.controller.modelListenerTest;

import it.polimi.ingsw.controller.modelListener.BookshelfListener;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class testing {@code BookshelfListener}.
 */
public class BookshelfListenerTest {
    /**
     * Testing for the constructor for the class
     */
    @Test
    public void testConstructor()
    {
        BookshelfListener bookshelfListener = new BookshelfListener("nick");
        assertEquals("nick", bookshelfListener.getOwner());
        assertNotNull(bookshelfListener.getBookshelfUpdates());
        assertTrue(bookshelfListener.getBookshelfUpdates().isEmpty());
    }
    /**
     * Testing for the updateState method working as intended
     */
    @Test
    public void testUpdateState()
    {
        BookshelfListener bookshelfListener = new BookshelfListener("nick");
        bookshelfListener.updateState(new Position(0,0), Item.CAT);
        bookshelfListener.updateState(new Position(1,1), Item.CUP);
        bookshelfListener.updateState(new Position(2,2), Item.BOOK);
        Map<Position,Item> map = bookshelfListener.getBookshelfUpdates();
        assertEquals(map.get(new Position(0,0)), Item.CAT);
        assertEquals(map.get(new Position(1,1)), Item.CUP);
        assertEquals(map.get(new Position(2,2)), Item.BOOK);
        assertTrue(bookshelfListener.getBookshelfUpdates().isEmpty());

    }
}
