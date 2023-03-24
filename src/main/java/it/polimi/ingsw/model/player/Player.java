package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPatternInterface;

import java.util.*;

/**
 * Class representing a Player.
 */

public class Player {
    private final String nickname;
    private final Bookshelf bookshelf;
    private final PersonalGoalCard personalGoalCard;
    private int personalScore;
    private int bookshelfScore;
    private final ScoringToken[] scoringToken;
    private ScoringToken endingToken;

    /**
     * Player's Constructor: it initializes scores to zero, tokens to null, and it creates a Bookshelf and a PersonalGoalCard.
     * @param nickname Player's nickname.
     * @param pattern Player's PersonalGoalCard pattern.
     */
    public Player(String nickname, PersonalGoalPatternInterface pattern) {
        this.nickname = nickname;
        this.bookshelf = new Bookshelf(6, 5);
        this.personalGoalCard = new PersonalGoalCard(pattern);
        this.personalScore = 0;
        this.bookshelfScore = 0;
        this.scoringToken = new ScoringToken[2];
        scoringToken[0] = null;
        scoringToken[1] = null;
        this.endingToken = null;
    }

    /**
     * Update the personal score of {@code this}.
     */

    public void updatePersonalScore(){
        personalScore = getPersonalGoalCard().getPersonalScore(getBookshelf());
    }

    /**
     * Update the bookshelf score of {@code this}.
     */

    public void updateBookshelfScore(){
        bookshelfScore = getBookshelf().getBookshelfScore();
    }

    /**
     * Add a {@code ScoringToken} to {@code this} checking token's type.
     * @param token {@code ScoringToken} obtained completing a {@code CommonGoalCard} or being the first to fill the {@code Bookshelf}.
     */

    public void addScoringToken(ScoringToken token){
        if (token.getType() == -1 && endingToken == null){
            endingToken = token;
        } else if (scoringToken[0] == null && token.getType() != -1) {
            scoringToken[0] = token;
        } else if (scoringToken[1] == null && token.getType() != -1 && token.getType() != scoringToken[0].getType()){
            scoringToken[1] = token;
        }
    }

    /**
     *Insert a list of {@code Item}s in the {@code Bookshelf} of {@code this} in the {@code column}.
     * @param items {@code Item}s that are taken from the {@code LivingRoomBoard}.
     * @param column a {@code column} selected by the Player.
     */

    public void insertTiles(List<Item> items, int column){
        if (getBookshelf().canInsert(items.size(), column))
            getBookshelf().insert(items, column);
    }

    /**
     * Get the public score of {@code this}.
     * @return sum of {@code ScoringToken}s.
     */
    public int getPublicScore(){
        return endingToken.getScore() + scoringToken[0].getScore() + scoringToken[1].getScore();
    }

    /**
     * Get the total score of {@code this}.
     * @return sum of {@code ScoringToken}s, {@code personalScore} and {@code bookshelfScore}.
     */
    public int getTotalScore(){
        return getPublicScore() + getPersonalScore() + getBookshelfScore();
    }

    /**
     * Get the {@code Bookshelf} of {@code this}.
     * @return {@code Bookshelf} of {@code this}.
     */
    public Bookshelf getBookshelf(){
        return bookshelf;
    }

    /**
     * Get the {@code PersonalGoalCard} of {@code this}.
     * @return {@code PersonalGoalCard} of {@code this}.
     */
    public PersonalGoalCard getPersonalGoalCard(){
        return personalGoalCard;
    }

    /**
     * Get the {@code personalScore} of {@code this}.
     * @return {@code personalScore} of {@code this}.
     */
    public int getPersonalScore(){
        return personalScore;
    }

    /**
     * Get the {@code bookshelfScore} of {@code this}.
     * @return {@code bookshelfScore} of {@code this}.
     */

    public int getBookshelfScore(){
        return bookshelfScore;
    }
}
