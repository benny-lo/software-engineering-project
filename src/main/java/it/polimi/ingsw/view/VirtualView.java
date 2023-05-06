package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.server.UpdateSender;
import it.polimi.ingsw.utils.networkMessage.server.*;
import it.polimi.ingsw.view.rep.*;

import java.rmi.RemoteException;
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
    private final ChatRep chatRep;
    private Controller controller;
    private UpdateSender toClient;
    private boolean error;

    public VirtualView(String nickname) {
        this.nickname = nickname;
        this.bookshelfRep = new ArrayList<>();
        this.livingRoomRep = new LivingRoomRep(9, 9);
        this.endingTokenRep = new EndingTokenRep();
        this.personalGoalRep = new PersonalGoalCardRep(nickname);
        this.commonGoalCardsRep = new CommonGoalCardsRep();
        this.itemsChosenRep = new ItemsChosenRep(nickname);
        this.chatRep = new ChatRep();
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
    public ChatRep getChatRep() {
        return chatRep;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void setToClient(UpdateSender toClient) {
        this.toClient = toClient;
    }

    public void sendBookshelves() {
        for(BookshelfRep rep : bookshelfRep) {
            if (rep.hasChanged()) {
                toClient.sendBookshelfUpdate(
                        new BookshelfUpdate(rep.getOwner(), rep.getBookshelf())
                );
            }
        }
    }

    public void sendLivingRoom() {
        if (!livingRoomRep.hasChanged()) return;
        toClient.sendLivingRoomUpdate(new LivingRoomUpdate(livingRoomRep.getLivingRoom()));
    }

    public void sendEndingToken() {
        if (!endingTokenRep.hasChanged()) return;
        toClient.sendEndingTokenUpdate(new EndingTokenUpdate(endingTokenRep.getEndingToken()));
    }

    public void sendPersonalGoalCard() {
        if (personalGoalRep.hasChanged()) return;
        toClient.sendPersonalGoalCardUpdate(new PersonalGoalCardUpdate(personalGoalRep.getPersonalGoalCard()));
    }

    public void sendCommonGoalCard() {
        if (!commonGoalCardsRep.hasChanged()) return;

        List<Integer> ids = commonGoalCardsRep.getIDs();
        List<Integer> tops = commonGoalCardsRep.getTops();
        for(int i = 0; i < Math.min(ids.size(), tops.size()); i++) {
            toClient.sendCommonGoalCardUpdate(new CommonGoalCardUpdate(ids.get(i), tops.get(i)));
        }
    }

    public void sendMessage() {
        if (!chatRep.hasChanged()) return;
        for(Message message : chatRep.getChat()) {
            toClient.sendChatUpdate(new ChatUpdate(message.getNickname(), message.getText()));
        }
    }

    // TODO: scores

    public void sendStartTurn() {
        try {
            toClient.sendStartTurnUpdate();
        } catch (RemoteException e) {
            System.out.println("lost connection to " + nickname);
        }
    }

    public void sendEndGame() {
        try{
            toClient.sendEndGameUpdate();
        } catch (RemoteException e) {
            System.out.println("lost connection to " + nickname);
        }
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
