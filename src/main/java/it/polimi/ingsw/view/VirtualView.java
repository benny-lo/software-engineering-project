package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.rep.*;

import java.util.ArrayList;
import java.util.List;

public class VirtualView {
    private final String nickname;
    private final List<BookshelfRep> bookshelfRep;
    private final LivingRoomRep livingRoomRep;
    private final EndingTokenRep endingTokenRep;
    private final PersonalGoalCardRep personalGoalRep;
    private final CommonGoalCardsRep commonGoalCardsRep;
    private final ItemsChosenRep itemsChosenRep;
    private Controller controller;
    private boolean error;

    public VirtualView(String nickname) {
        this.nickname = nickname;
        this.bookshelfRep = new ArrayList<>();
        this.livingRoomRep = new LivingRoomRep(9, 9);
        this.endingTokenRep = new EndingTokenRep();
        this.personalGoalRep = new PersonalGoalCardRep(nickname);
        this.commonGoalCardsRep = new CommonGoalCardsRep();
        this.itemsChosenRep = new ItemsChosenRep(nickname);
        this.controller = null;
        this.error = false;
    }

    public String getNickname() {
        return nickname;
    }

    public List<BookshelfRep> getBookshelfRep() {
        return bookshelfRep;
    }

    public LivingRoomRep getLivingRoomRep() {
        return livingRoomRep;
    }

    public EndingTokenRep getEndingTokenRep() {
        return endingTokenRep;
    }

    public PersonalGoalCardRep getPersonalGoalRep() {
        return personalGoalRep;
    }

    public CommonGoalCardsRep getCommonGoalCardsRep() {
        return commonGoalCardsRep;
    }

    public ItemsChosenRep getItemsChosenRep() {
        return itemsChosenRep;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void setError() {
        error = true;
    }

    public boolean isError() {
        boolean ret = error;
        error = false;
        return ret;
    }
}
