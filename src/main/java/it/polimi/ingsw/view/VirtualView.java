package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Item;

public class VirtualView {
    private final String nickname;
    private Item[][] bookshelfRep;
    private Item[][] livingRoomRep;
    private boolean endingTokenRep;
    private int personalGoalRep;

    private Controller controller;

    public VirtualView(String nickname) {
        this.nickname = nickname;
        this.controller = null;
    }

    public String getNickname() {
        return nickname;
    }


}
