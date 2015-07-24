package com.mahyaar.holdon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import java.util.Random;


public class MainActivity extends Activity {

    private static final int maxSeed = 5;
    private static final int minSeed = 3;
    public static final String My_PREFS = "MY_PREFS";
    private int seedIncrement = 1;
    private TextSwitcher messageSwitch;
    private Button holdButton;
    private int holdTime;
    private TextView scoreEditText;
    private TextView bestScoreEditText;
    private int accuracyMiliSeconds = 2000;
    private int score;
    private int bestScore;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageSwitch = (TextSwitcher) findViewById(R.id.messageSwitcher);
        messageSwitch.setFactory(myFactory);
        messageSwitch.setCurrentText(holdMessage(maxSeed, minSeed));
        holdButton = (Button) findViewById(R.id.button_hold);
        scoreEditText = (TextView) findViewById(R.id.scoreEditText);
        scoreEditText.setText("Current score: " + String.valueOf(score));
        bestScoreEditText = (TextView) findViewById(R.id.bestScoreEditText);

        bestScoreEditText.setText("Current best score: " + String.valueOf(getBestScore()));


        holdButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long timeDown = event.getDownTime();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holdButton.setBackgroundColor(Color.YELLOW);
                    holdButton.setText("Hold!");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    long timeUp = event.getEventTime();
                    long duration = timeUp - timeDown;

                    if (duration >= holdTime * 1000 - accuracyMiliSeconds && duration <= holdTime * 1000 + accuracyMiliSeconds) {
                        increaseScore(seedIncrement * 10);
                        messageSwitch.setText("Good job! now " + holdMessage(maxSeed + seedIncrement, minSeed + seedIncrement));
                        scoreEditText.setText("Current score: " + String.valueOf(score));
                        holdButton.setBackgroundColor(Color.GREEN);
                        holdButton.setText("Press and Hold");
                        holdButton.setTypeface(null, Typeface.BOLD);
                        seedIncrement++;
                        if (score > bestScore) {
                            bestScore = score;
                            bestScoreEditText.setText("Best score: " + String.valueOf(bestScore));

                        }
                    } else {
                        messageSwitch.setText("You lost! Try again!");
                        saveBestScore();
                        resetScore();

                        //This is how we wait for 2.5 seconds before rerunning the onCreate()
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //This is the trick to restart the onCreate() method.
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        }, 2500);

                    }

                }

                return true;
            }
        });
    }


    private String holdMessage(int max, int min) {
        Random rand = new Random();
        holdTime = rand.nextInt((max - min) + 1) + min;
        String message = "Press and hold for exactly " + String.valueOf(holdTime) + " seconds!";
        return message;
    }

    private ViewFactory myFactory = new ViewFactory() {
        @Override
        public View makeView() {
            TextView myText = new TextView(MainActivity.this);
            myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            myText.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_DeviceDefault_Small);
            return myText;
        }
    };

    private void increaseScore(int a){
        score += a;
    }

    private void resetScore(){
        score = 0;
    }

    private int getBestScore(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(My_PREFS, 0);
        bestScore = preferences.getInt("BestScore", bestScore);
        return bestScore;

    }
    @Override
    protected void onStop() {
        super.onStop();
        saveBestScore();
    }

    private void saveBestScore() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(My_PREFS, 0);
        SharedPreferences.Editor editor = preferences. edit();
        editor.putInt("BestScore", bestScore);
        editor.commit();
    }
}
