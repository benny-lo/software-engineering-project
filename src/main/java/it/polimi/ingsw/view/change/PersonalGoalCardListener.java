package it.polimi.ingsw.view.change;

public class PersonalGoalCardListener extends ModelListener {
    private int id;
    String owner;

    public PersonalGoalCardListener(String owner) {
        super();
        this.owner = owner;
    }

    public int getPersonalGoalCard() {
        changed = false;
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void updateState(int id) {
        changed = true;
        this.id = id;
    }
}
