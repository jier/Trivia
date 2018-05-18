package com.example.jier.trivia;

import android.app.Activity;
import android.content.Context;
import android.telecom.Call;
import android.util.Log;
import android.view.ViewStructure;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * This is a listener class to any value that will be entered/retrieved to/from the Firebasedatabase.
 * By using the reference of the database as a global and its reference highscores objects are fetch
 * and post through callback to the highScore activity.
 *
 * Created by jier on 15-3-18.
 */

public class HighscoreHelper implements ValueEventListener {


    public interface Callback {

        void gotHighScore(ArrayList<HighScore> highScores);
        void gotError(String msg);
    }

    // global
    private Callback highscoreHelper;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    HighScore helperHighscoreObject;

    public HighscoreHelper(Context aContext, Activity anActivity) {

        context = aContext;
        highscoreHelper = (Callback) anActivity;

        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

    }

    // get highscores by accessing root of the database and order these

    public void getHighScore() {

        DatabaseReference reference = dbReference.child("highscores");
        Query query = reference.orderByChild("score");
        query.addValueEventListener(this);

    }

    // update the database with highscore objects by putting specific timestamp to avoid users with
    // same name
    public void postHighScore(HighScore aScore) {

        helperHighscoreObject = aScore;
        String actualTime = String.valueOf(Calendar.getInstance().getTime());
        dbReference.child("highscores").child(actualTime).setValue(aScore);
    }

    // Show content of the database by its snapshot and pass it to the callback through arraylist
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        ArrayList<HighScore> scoreArrayList = new ArrayList<>();
        HashMap hashMap;
        String name;
        long score;

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

            hashMap = (HashMap) snapshot.getValue();
            name = (String) hashMap.get("name");
            score = (long) hashMap.get("score");

            HighScore highScore = new HighScore(name,score);
            scoreArrayList.add(highScore);
        }
        Collections.reverse(scoreArrayList); // reverse order from ascending to descending
        highscoreHelper.gotHighScore(scoreArrayList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        highscoreHelper.gotError(databaseError.getMessage());
    }
}
