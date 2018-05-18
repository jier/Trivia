package com.example.jier.trivia;

import java.io.Serializable;

/**
 * This is class object to represent a highschore of a player. Thus the name and the score
 * of the player should be stored in this object.
 *
 * Created by jier on 15-3-18.
 */

public class HighScore implements Serializable {

    private String name;
    private long score;

    public HighScore(String aName, long aScore) {
        name = aName;
        score = aScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
