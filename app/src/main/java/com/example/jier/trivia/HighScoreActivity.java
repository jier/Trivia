package com.example.jier.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.ArrayList;

/**
 * This class class calls upon the firebase database through a callback of Highscorehelper.
 * Thus, it retrieve data or post it when the bundlestate is not null. This safety check is to
 * avoid unwanted behaviour while rotating the phone to post scores to the database when getting
 * the intent from the game activity.
 *
 *
 * Jier Nzuanzu
 */

public class HighScoreActivity extends AppCompatActivity implements HighscoreHelper.Callback {

    // global
    HighScore highScore;
    ListView listViewHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // make sure to post only when savedInstancestate is null other whise get the highscore
        if (savedInstanceState == null) {

            Intent gameIntent = getIntent();
            highScore = (HighScore) gameIntent.getSerializableExtra("highscore");
            new HighscoreHelper(getApplicationContext(),this).postHighScore(highScore);
        }

        new HighscoreHelper(getApplicationContext(),this).getHighScore();
    }

    @Override
    public void gotHighScore(ArrayList<HighScore> highScores) {

        // set the view through adapter
        listViewHighScore = findViewById(R.id.highscoreList);
        HighScoreAdapter adapterView = new HighScoreAdapter(this,R.layout.items, highScores);
        listViewHighScore.setAdapter(adapterView);
    }

    @Override
    public void gotError(String msg) {

        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }
}
