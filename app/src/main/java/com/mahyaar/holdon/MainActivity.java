package com.mahyaar.holdon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import java.util.Random;


public class MainActivity extends Activity {

    private static final int maxSeed = 5;
    private static final int minSeed = 3;
    private TextSwitcher messageSwitch;
    private Button holdButton;
    private int holdTime;
    private EditText scoreEditText;
    int seedIncrement = 1;
    private int score;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageSwitch = (TextSwitcher) findViewById(R.id.messageSwitcher);
        messageSwitch.setFactory(myFactory);
        messageSwitch.setCurrentText(holdMessage(maxSeed, minSeed));
        holdButton = (Button) findViewById(R.id.button_hold);
        scoreEditText = (EditText) findViewById(R.id.scoreEditText);
        scoreEditText.setText("Current score: " + String.valueOf(score));

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

                    if (duration >= holdTime * 1000 - 20000  && duration <= holdTime * 1000 + 20000) {
                        increaseScore(seedIncrement * 10);
                        messageSwitch.setText("Good job! now " + holdMessage(maxSeed + seedIncrement, minSeed + seedIncrement));
                        scoreEditText.setText("Current score: " + String.valueOf(score));
                        holdButton.setBackgroundColor(Color.GREEN);
                        holdButton.setText("Press and Hold");
                        holdButton.setTypeface(null, Typeface.BOLD);
                        seedIncrement++;
                    } else {
                        messageSwitch.setText("You lost! Try again!");
                        resetScore();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
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
}
