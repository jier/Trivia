package com.example.jier.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {

    Question questionCurrent;
    EditText answers;
    int score, questionCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent game = getIntent();
        questionCount = game.getIntExtra("nextGame",0);

        if (questionCount == 3) {
            Intent scoreBoard = new Intent(GameActivity.this, HighScoreActivity.class);
            scoreBoard.putExtra("highScore",score);
        }
        new TriviaHelper(getApplicationContext()).getQuestion(this);

        Button checkAnswer = findViewById(R.id.Check);
        Button quitGame =  findViewById(R.id.quit);
        Button  nextGame = findViewById(R.id.next);

        answers = findViewById(R.id.answer);

        checkAnswer.setOnClickListener(new CheckAnswerListener());
        quitGame.setOnClickListener(new QuitGameListener());
        nextGame.setOnClickListener(new NextGameListener());
    }

    @Override
    public void gotQuestion(ArrayList<Question> question) {

        questionCurrent = question.get(0);
        String q = questionCurrent.getQuestion();
        TextView questionView  = findViewById(R.id.question);
        questionView.setText(q);

        System.out.println(question.get(0).getQuestion());
        System.out.println(question.get(0).getCorrectAnswer());
    }

    @Override
    public void gotError(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private class CheckAnswerListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String viewAnswer = String.valueOf(answers.getText());
            System.out.println(viewAnswer);
            try {
                String correctAnswer = questionCurrent.getCorrectAnswer();
                int numQuestions = questionCurrent.getNumOfQuestion();
                String[] ansArray = new String[numQuestions];

                for (int i = 0; i < numQuestions ; i++) {
                    ansArray[i] = viewAnswer;
                    questionCurrent.setAnswer(ansArray[i]);
                }

                if (correctAnswer.contains(viewAnswer) || correctAnswer.equalsIgnoreCase(viewAnswer)) {
                    System.out.println("Equals bitches");
                    score += 1;
                }

            }catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
    }

    private class QuitGameListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent quitIntent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(quitIntent);
        }
    }

    private class NextGameListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent nextGame = new Intent(GameActivity.this, GameActivity.class);
            questionCount += 1;
            nextGame.putExtra("nextGame",questionCount);
            startActivity(nextGame);
        }
    }

    @Override
    public void onBackPressed() {

        return;
    }
}
