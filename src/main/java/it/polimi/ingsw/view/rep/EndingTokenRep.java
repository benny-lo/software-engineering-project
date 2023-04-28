package it.polimi.ingsw.view.rep;

public class EndingTokenRep extends Rep {
    private String owner;

    public EndingTokenRep() {
        super();
        this.owner = null;
    }

    public String getEndingToken() {
        peek();
        return owner;
    }

    public void updateRep(String owner) {
        update();
        this.owner = owner;
    }
}
