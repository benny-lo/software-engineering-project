package it.polimi.ingsw.view;

import it.polimi.ingsw.utils.message.server.*;

/**
 * Interface to process updates received.
 */
public interface UpdateViewInterface {
    /**
     * Processes a {@code GameList} message.
     * @param message The message to process.
     */
    void onGamesList(GamesList message);

    /**
     * Processes a {@code GameData} message.
     * @param message The message to process.
     */
    void onGameData(GameData message);

    /**
     * Processes a {@code SelectedItems} message.
     * @param message The message to process.
     */
    void onSelectedItems(SelectedItems message);

    /**
     * Processes a {@code AcceptedInsertion} message.
     * @param message The message to process.
     */
    void onAcceptedInsertion(AcceptedInsertion message);

    /**
     * Processes a {@code ChatAccepted} message.
     * @param message The message to process.
     */
    void onChatAccepted(ChatAccepted message);

    /**
     * Processes a {@code LivingRoomUpdate}.
     * @param update The update to process.
     */
    void onLivingRoomUpdate(LivingRoomUpdate update);

    /**
     * Processes a {@code BookshelfUpdate}.
     * @param update The update to process.
     */
    void onBookshelfUpdate(BookshelfUpdate update);

    /**
     * Processes a {@code WaitingUpdate}.
     * @param update The update to process.
     */
    void onWaitingUpdate(WaitingUpdate update);

    /**
     * Processes a {@code ScoresUpdate}.
     * @param update The update to process.
     */
    void onScoresUpdate(ScoresUpdate update);

    /**
     * Processes a {@code EndingTokenUpdate}.
     * @param update The update to process.
     */
    void onEndingTokenUpdate(EndingTokenUpdate update);

    /**
     * Processes a {@code CommonGoalCardsUpdate}.
     * @param update The update to process.
     */
    void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update);

    /**
     * Processes a {@code PersonalGoalCardUpdate}.
     * @param update The update to process.
     */
    void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update);

    /**
     * Processes a {@code ChatUpdate}.
     * @param update The update to process.
     */
    void onChatUpdate(ChatUpdate update);

    /**
     * Processes a {@code StartTurnUpdate}.
     * @param update The update to process.
     */
    void onStartTurnUpdate(StartTurnUpdate update);

    /**
     * Processes a {@code EndGameUpdate}.
     * @param update The update to process.
     */
    void onEndGameUpdate(EndGameUpdate update);
}
