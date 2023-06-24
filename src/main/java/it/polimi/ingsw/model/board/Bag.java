package it.polimi.ingsw.model.board;

import it.polimi.ingsw.utils.game.Item;

import java.util.Collections;
import java.util.Stack;

/**
 * Class representing the Bag in the game. It contains {@code Item}s.
 */
public class Bag {
    /**
     * Stack containing all currently remaining {@code Item}s in {@code this}.
     */
    private final Stack<Item> elements;

    /**
     * Constructor for {@code this} class. Fills {@code this} and randomly shuffles it.
     * @param numberPerType Number of {@code Item}s of each type to put in {@code this}.
     */
    public Bag(int numberPerType) {
        this.elements = new Stack<>();
        for(Item item : Item.values()) {
            if (item == Item.LOCKED) continue;
            for(int i = 0; i < numberPerType; i++) elements.push(item);
        }
        Collections.shuffle(elements);
    }

    /**
     * Check if {@code this} is empty.
     * @return {@code true} iff {@code this} is empty.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Extract an {@code Item} from {@code this}.
     * @return The {@code Item} extracted if {@code this} was not empty, else null.
     */
    public Item extract() {
        if (!this.isEmpty()) return elements.pop();
        else return null;
    }
}
