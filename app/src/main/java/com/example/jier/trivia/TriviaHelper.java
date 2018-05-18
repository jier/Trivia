package com.example.jier.trivia;

import android.content.Context;
import android.text.Html;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class is a response class that will handle the callback for the game activity while connecting to the
 * API to fetch data. Because of some internet connection issues, try and catch are applied to retrieve
 * a question.
 *
 * Created by jier on 12-3-18.
 */

public class TriviaHelper implements Response.Listener<JSONArray>, Response.ErrorListener {

    public interface Callback {
        void gotQuestion(ArrayList<Question> question);
        void gotError(String msg);
    }

    private Callback triviaHelper;
    Context context;

    public TriviaHelper (Context aContext) {
        context = aContext;
    }

    public void getQuestion(Callback activity){

        this.triviaHelper = activity;
        RequestQueue queue ;
        String url;

        queue = Volley.newRequestQueue(context);
        url = "http://jservice.io/api/random";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onResponse(JSONArray response) {

        String aQuestion, anAnswer;
        int maximumPoint;
        ArrayList<Question> questionArrayList = new ArrayList<>();

        try {

            JSONObject responseObject = response.getJSONObject(0);
            aQuestion = responseObject.getString("question");
            anAnswer = Html.escapeHtml(responseObject.getString("answer"));
            maximumPoint = responseObject.getInt("value");

            Question questionObject = new Question(aQuestion,anAnswer);
            questionObject.setMaxValue(maximumPoint);
            questionArrayList.add(questionObject);

        } catch (JSONException e) {

            e.printStackTrace();
            triviaHelper.gotError("Oeps failed to connect to Internet, wait a seconde");
            getQuestion(triviaHelper);
            return;
        }
        triviaHelper.gotQuestion(questionArrayList);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        triviaHelper.gotError(error.getMessage());
    }


}
