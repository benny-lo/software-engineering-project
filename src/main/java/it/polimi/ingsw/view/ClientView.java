package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.client.RequestSender;
import it.polimi.ingsw.network.client.UpdateReceiver;
import it.polimi.ingsw.utils.Rank;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.util.List;
import java.util.Map;

public abstract class ClientView implements UpdateReceiver {
    protected String nickname;
    protected int numberPlayers;
    protected int numberCommonGoalCards;
    protected Item[][] livingRoom;
    protected Map<String, Item[][]> bookshelves;
    protected String endingToken;
    protected int personalGoalCardId;
    protected List<int[]> commonGoalCards;
    protected List<Rank> scores;
    protected boolean endGame;
    protected List<Message> chat;
    protected RequestSender sender;

    public abstract void onAcceptedAction(AcceptedAction message);
    public abstract void onBookshelfUpdate(BookshelfUpdate update);
    public abstract void onChatUpdate(ChatUpdate update);
    public abstract void onCommonGoalCardUpdate(CommonGoalCardUpdate update);
    public abstract void onEndGameUpdate(EndGameUpdate update);
    public abstract void onEndingTokenUpdate(EndingTokenUpdate update);
    public abstract void onGamesList(GamesList gamesList);
    public abstract void onItemsSelected(ItemsSelected itemsSelected);
    public abstract void onLivingRoomUpdate(LivingRoomUpdate update);
    public abstract void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update);
    public abstract void onScoresUpdate(ScoresUpdate update);
    public abstract void onStartTurnUpdate(StartTurnUpdate update);
    public abstract void onWaitingUpdate(WaitingUpdate update);
}
