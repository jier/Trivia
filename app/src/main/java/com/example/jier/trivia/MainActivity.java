package com.example.jier.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * This is the main activity that focuses on enabling the user to go to the leaderboard,
 * resume its game if already played and while playing use the quit button to extract the intent.
 * Or to start a new game.
 *
 * Jier Nzuanzu
 *
 */
public class MainActivity extends AppCompatActivity {

    Bundle resuming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart  = findViewById(R.id.startGame);
        Button buttonScoreBoard = findViewById(R.id.scoreBord);
        Button buttonResumeGame = findViewById(R.id.resumeGame);

        Intent resumeGame = getIntent();
        resuming = resumeGame.getBundleExtra("quitting");

        buttonStart.setOnClickListener(new StartGameListener());
        buttonResumeGame.setOnClickListener(new ResumeGameListener());
        buttonScoreBoard.setOnClickListener(new StartScoreBoardListener());

    }

    private class StartGameListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent intentGame = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intentGame);

        }
    }

    private class ResumeGameListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent intentResume = new Intent(MainActivity.this, GameActivity.class);
            intentResume.putExtra("resume",resuming);
            startActivity(intentResume);
        }
    }

    private class StartScoreBoardListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent intentScoreBoard = new Intent(MainActivity.this, HighScoreActivity.class);
            startActivity(intentScoreBoard);
        }
    }
}
