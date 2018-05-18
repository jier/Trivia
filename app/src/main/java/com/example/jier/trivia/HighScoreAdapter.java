package com.example.jier.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This adapter class just need to put the view on the screen retrieved from the highscore activity
 * by extracting the Arraylist of highscores.
 *
 * Created by jier on 16-3-18.
 */

public class HighScoreAdapter extends ArrayAdapter<HighScore> {

    private ArrayList<HighScore> scores;
    private Context theContext;

    public HighScoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HighScore> objects) {
        super(context, resource, objects);
        scores = objects;
        theContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items,parent,false);
        }

        HighScore items = scores.get(position);
        int position_player = position + 1;
        String name = items.getName();
        long score = items.getScore();

        TextView player_id = convertView.findViewById(R.id.player_id);
        TextView player_name = convertView.findViewById(R.id.player);
        TextView player_score = convertView.findViewById(R.id.points);


        player_id.setText(String.valueOf(position_player));
        player_name.setText(name);
        player_score.setText(String.valueOf(score));


        return  convertView;
    }
}
