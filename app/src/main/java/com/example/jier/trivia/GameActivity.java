package com.example.jier.trivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class is where the actual logic of the game is implemented and send through main or highscore
 * activity. The game activity  enables the player to play on resume  by sending an intent of bundle to the main activity
 * resume button and the latter will send it back on create. Safety check are made to be able to continue
 * the game where it was closed by retrieve the score, the question and which question count it was.
 *
 * Furthermore, the game checks on create when phone is rotated that the same question is restored
 * when the phone is rotated back.
 * Both the rotation and resume checks set back the views on the screen thanks to global variables.
 *
 */

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {

    // global
    Question questionCurrent;
    EditText answers;
    int  hintCount = 0;
    int  questionCount, retryCount,maxAnswerValue;
    long score;
    final int retryCountMax = 3;
    final int MAX_GAME = 5;
    boolean correctAnswered = false;
    HighScore highScore;
    TextView questionView;
    String q;
    String hint = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent game = getIntent();
        Intent resumeGame = getIntent();
        Bundle resumed = resumeGame.getBundleExtra("resume");

        questionCount = game.getIntExtra("nextGame",0);
        score = game.getLongExtra("score",0);

        // Enter user name in score baord.
        if (questionCount == MAX_GAME) {
            updateScoreBoard();
            return;
        }

        Button checkAnswer = findViewById(R.id.Check);
        Button quitGame =  findViewById(R.id.quit);
        Button  hintGame = findViewById(R.id.hint);
        answers = findViewById(R.id.answer);

        checkAnswer.setOnClickListener(new CheckAnswerListener());
        quitGame.setOnClickListener(new QuitGameListener());
        hintGame.setOnClickListener(new HintGameListener());

        // Check that the view stay the same when rotated
        if (savedInstanceState != null) {

            questionCurrent = (Question) savedInstanceState.getSerializable("question");
            questionCount = savedInstanceState.getInt("questionCount");
            score = savedInstanceState.getLong("totalScore");


            q = questionCurrent.getQuestion();
            questionView  = findViewById(R.id.question);
            questionView.setText(q);

        } else if (resumed != null)  { // play where user left off

            questionCurrent = (Question) resumed.getSerializable("the_question");
            questionCount = resumed.getInt("the_nth_question");
            score = resumed.getLong("the_total_score");

            q = questionCurrent.getQuestion();
            questionView  = findViewById(R.id.question);
            questionView.setText(q);

        } else {

            new TriviaHelper(getApplicationContext()).getQuestion(this);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putSerializable("question",questionCurrent);
        outState.putInt("questionCount",questionCount);
        outState.putLong("totalScore",score);
    }

    @Override
    public void gotQuestion(ArrayList<Question> question) {

        questionCurrent = question.get(0);
        q = questionCurrent.getQuestion();
        questionView  = findViewById(R.id.question);
        questionView.setText(q);
    }

    @Override
    public void gotError(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    // Check answer and pop-up dialog view to  show user if answer is correct or if a retry is needed.
    private class CheckAnswerListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String viewAnswer = String.valueOf(answers.getText());
            String correctAnswer = questionCurrent.getCorrectAnswer();

            int numQuestions = questionCurrent.getNumOfQuestion();
            final int maxPunishRetryValueCount = 100;
            maxAnswerValue = questionCurrent.getMaxValue();

            String msgDialog  ;

            if (correctAnswer.equalsIgnoreCase(viewAnswer) || correctAnswer.equals(viewAnswer)) {

                msgDialog = "Nicely done";
                score += maxAnswerValue - (maxPunishRetryValueCount * retryCount); // punish if users retry
                correctAnswered = true;
            } else {
                msgDialog = "Wrong answer";
                retryCount += 1;
                correctAnswered = false;
            }

            AlertDialog.Builder checkDialog = new AlertDialog.Builder(GameActivity.this);

            checkDialog.setMessage(msgDialog);
            checkDialog.setCancelable(false);

            if (retryCount < retryCountMax && !correctAnswered)  {
                checkDialog.setNegativeButton(
                    "Retry",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            answers.setText("");
                            dialogInterface.cancel();
                        }
                    });
            }

            checkDialog.setPositiveButton(
                    "Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent nextGame = new Intent(GameActivity.this, GameActivity.class);
                            questionCount += 1;
                            nextGame.putExtra("nextGame",questionCount);
                            nextGame.putExtra("score", score);
                            startActivity(nextGame);
                        }
                    }
            );
            AlertDialog alertDialog = checkDialog.create();

            alertDialog.show();
        }
    }

    // Send information back to the main activity of the game state

    private class QuitGameListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent quitIntent = new Intent(GameActivity.this, MainActivity.class);
            Bundle saveGame = new Bundle();

            saveGame.putSerializable("the_question",questionCurrent);
            saveGame.putInt("the_nth_question",questionCount);
            saveGame.putLong("the_total_score",score);

            quitIntent.putExtra("quitting",saveGame);
            startActivity(quitIntent);
        }
    }

    // Show hint to user but lessen the score so far according the amount of help the user need
    private class HintGameListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {

            hint += String.valueOf(questionCurrent.getCorrectAnswer().charAt(hintCount));
            hintCount += 1;
            if (hintCount == questionCurrent.getCorrectAnswer().length()) {
                score = 0;
            } else {
                score -= (maxAnswerValue /hintCount);
            }

            Toast.makeText(getApplicationContext(),String.valueOf(hint),Toast.LENGTH_SHORT).show();
        }
    }

    // enable user to add its name to the scoreboard by a dialog and send this intent to the highscoreactivity
    public void updateScoreBoard() {

        final EditText nameUser = new EditText(this);
        final String[] user = new String[2];

        AlertDialog.Builder nameDialog = new AlertDialog.Builder(GameActivity.this);


        nameDialog.setMessage("Enter your name for the Highscore board");
        nameDialog.setView(nameUser);
        nameDialog.setCancelable(false);

        nameDialog.setPositiveButton(
            "Submit",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                     user[0] = String.valueOf(nameUser.getText());
                    highScore = new HighScore(user[0],score);
                    Intent scoreBoard = new Intent(GameActivity.this, HighScoreActivity.class);
                    scoreBoard.putExtra("highscore", highScore);

                    startActivity(scoreBoard);
                }
            }
        );
        nameDialog.show();
    }

    // user may not use the back button of the phone itself unless it uses the quit button.
    @Override
    public void onBackPressed() {

        return;
    }
}
