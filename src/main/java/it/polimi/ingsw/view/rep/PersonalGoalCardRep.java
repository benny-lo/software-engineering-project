package it.polimi.ingsw.view.rep;

public class PersonalGoalCardRep extends Rep {
    private int id;
    String owner;

    public PersonalGoalCardRep(String owner) {
        super();
        this.owner = owner;
        this.id = -1;
    }

    public int getPersonalGoalCard() {
        peek();
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void updateRep(int id) {
        update();
        this.id = id;
    }
}
