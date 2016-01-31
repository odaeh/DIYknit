package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.models.BoardModel;
import sheep.game.Game;

public class ExistingPatternActivity extends Activity {

    private BoardModel bModel;
    private TextView test;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button backButton;
    private Resources resource;
    private String patternID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing);

        Intent intent = getIntent();
        this.bModel = intent.getParcelableExtra("boardmodel");
        //this.patternID = intent.getStringExtra("patternID");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.existingView);

        setViewElements();

    } //OnCreate

    private void setViewElements() {
        button1 = (Button) findViewById(R.id.choosePattern1);
       // if (resource.borders.size() == 0){
        if (resource.fifo.size() == 0){
            button1.setText("");
        }
        else{
           // button1.setText(resource.borders.get(0));
            patternID = resource.fifo.pop().toString();
            button1.setText(patternID);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingPatternActivity.this, RecipeActivity.class);
                startActivity(intent);
            }
        });

        button2 = (Button) findViewById(R.id.choosePattern2);
       // if (resource.borders.size() == 0){
        if (resource.fifo.size() == 0){
            button2.setText("");
        }
        else{
            //button2.setText(resource.borders.get(1));
            patternID = resource.fifo.pop().toString();
            button2.setText(patternID);
        }
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button3 = (Button) findViewById(R.id.choosePattern3);
        //if (resource.borders.size() == 0){
        if (resource.fifo.size() == 0){
            button3.setText("");
        }
        else{
            //button3.setText(resource.borders.get(1));
            patternID = resource.fifo.pop().toString();
            button3.setText(patternID);
        }
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingPatternActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}