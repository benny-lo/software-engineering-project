package it.polimi.ingsw.view.change;

public class EndingTokenListener extends ModelListener {
    private String owner;

    public EndingTokenListener() {
        super();
        this.owner = null;
    }

    public String getEndingToken() {
        changed = false;
        String ret = owner;
        owner = null;

        return ret;
    }

    public void updateState(String owner) {
        changed = true;
        this.owner = owner;
    }
}
