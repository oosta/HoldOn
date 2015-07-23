package com.mahyaar.holdon;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import java.util.Random;


public class MainActivity extends Activity {

    private static final String TAG = "Mahyar";
    private TextSwitcher messageSwitch;
    private Button holdButton;
    private int holdTime;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageSwitch = (TextSwitcher) findViewById(R.id.messageSwitcher);
        messageSwitch.setFactory(myFactory);
        messageSwitch.setCurrentText(holdMessage(5, 3));
        holdButton = (Button) findViewById(R.id.button_hold);

//        holdButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                messageSwitch.setText("You lost! You did not hold for " + String.valueOf(holdTime));
//            }
//        });

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

                    if (holdTime * 1000 - 500 <= duration && duration <= holdTime * 1000 + 500) {
                        score = +1;
                        messageSwitch.setText("Good job! now " + holdMessage(5 + score, 3 + score));
                    } else {
                        messageSwitch.setText("You lost! Try again!");
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
}
