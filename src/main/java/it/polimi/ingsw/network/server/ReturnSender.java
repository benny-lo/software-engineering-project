package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.networkMessage.server.GamesList;
import it.polimi.ingsw.utils.networkMessage.server.ItemsSelected;

public interface ReturnSender extends UpdateSender {
    void sendListOfGames(GamesList gamesList);
    void sendItemsSelection(ItemsSelected itemsSelected);
}
