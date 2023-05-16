package it.polimi.ingsw.view.change;

public abstract class ModelListener {
    protected boolean changed;

    public ModelListener() {
        changed = false;
    }

    public boolean hasChanged() {
        return changed;
    }
}
