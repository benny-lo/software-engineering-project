package it.polimi.ingsw.model;

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
     * Constructor of the class: fills {@code this} and randomly shuffles it.
     * @param numberPerType number of {@code Item}s of each type to put in {@code this}.
     */
    public Bag(int numberPerType) {
        this.elements = new Stack<>();
        Item[] items = Item.values();
        for (int j = 0; j < 6; j++) {
            Item type = items[j];
            for (int i = 0; i < numberPerType; i++) {
                elements.add(type);
            }
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
     * @return the {@code Item} extracted if {@code this} was not empty, else null.
     */
    public Item extract() {
        if (!this.isEmpty()) return elements.pop();
        else return null;
    }

    public void printBag() {
        for (Item item : elements) {
            System.out.println(item);
        }
    }
}
