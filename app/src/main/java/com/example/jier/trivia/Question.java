package com.example.jier.trivia;

import android.widget.Toast;

import java.io.Serializable;

/**
 * This is a class where Question object is created to be able to hold the question and the answer
 * of the question. The maximum question that will be used to play the game eventually is set
 * manually to 5, but this can be changed.
 * Created by jier on 12-3-18.
 */

public class Question implements Serializable {

    private String question;
    private String correctAnswer;
    private static final int numOfQuestion = 5;
    private int maxValue;
    private String[] answer = new String[numOfQuestion];

    public Question(String aQuestion, String aCorrectAnswer) {

        question = aQuestion;
        correctAnswer = aCorrectAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public int getNumOfQuestion() {
        return numOfQuestion;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }




    public void setMaxValue (int aMaxValue) {
        maxValue = aMaxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setAnswer(String answer) {
        try {
            for (int i = 0; i < numOfQuestion; i++) {
                this.answer[i] = answer;
            }
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }
}
