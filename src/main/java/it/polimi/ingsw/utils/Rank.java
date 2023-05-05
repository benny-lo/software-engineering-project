package it.polimi.ingsw.utils;

import java.io.Serializable;

public class Rank implements Serializable {
    private final String nickname;
    private final int score;

    public Rank(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }
}
