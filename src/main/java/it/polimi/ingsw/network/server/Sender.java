package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.server.UpdateSender;
import it.polimi.ingsw.utils.networkMessage.server.AcceptedAction;
import it.polimi.ingsw.utils.networkMessage.server.GamesList;
import it.polimi.ingsw.utils.networkMessage.server.ItemsSelected;

public interface Sender extends UpdateSender {
    void sendListOfGames(GamesList gamesList);
    void sendItemsSelection(ItemsSelected itemsSelected);
    void sendAccepted(AcceptedAction acceptedAction);
}
