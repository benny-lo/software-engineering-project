package it.polimi.ingsw.view.rep;

public abstract class Rep {
    private boolean change;

    public Rep() {
        change = false;
    }

    public boolean hasChanged() {
        return change;
    }

    public void peek() {
        change = false;
    }

    public void update() {
        change = true;
    }
}
