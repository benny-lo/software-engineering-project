package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.Stack;

public class Bag {
    private final Stack<Item> elements;

    public Bag(int numberPerType) {
        this.elements = new Stack<>();
        for (Item type : Item.values()) {
            for (int i = 0; i < numberPerType; i++) {
                elements.add(type);
            }
        }
        Collections.shuffle(elements);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }


    public Item extract() {
        if (!this.isEmpty()) return elements.pop();
        else return null;
    }
}
