package it.polimi.ingsw.view.rep;

public class PersonalGoalCardRep extends Rep {
    private int id;

    public PersonalGoalCardRep(int id) {
        super();
        this.id = id;
    }

    public int getPersonalGoalCard() {
        peek();
        return id;
    }

    public void updateRep(int id) {
        this.id = id;
    }
}
