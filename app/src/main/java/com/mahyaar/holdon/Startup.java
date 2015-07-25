package com.mahyaar.holdon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Mahyar on 7/25/2015.
 */
public class Startup extends Activity {

    private Button holdOn, game2, game3, game4, game5, game6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamemenu);

        holdOn = (Button) findViewById(R.id.buttonHoldOn);
        holdOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Startup.this,HoldOn.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Startup.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
