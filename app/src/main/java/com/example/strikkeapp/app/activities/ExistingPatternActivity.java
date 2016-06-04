package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.models.BoardModel;

public class ExistingPatternActivity extends Activity {

    private BoardModel bModel;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button backButton;
    private String patternID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing);

        Intent intent = getIntent();
        this.bModel = intent.getParcelableExtra("boardmodel");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.existingView);

        setViewElements();

    } //OnCreate

    private void setViewElements() {
        button1 = (Button) findViewById(R.id.choosePattern1);
        button2 = (Button) findViewById(R.id.choosePattern2);
        button3 = (Button) findViewById(R.id.choosePattern3);

        // Top button
        if (Resources.fifoSavedRecipes.isEmpty()){
            button3.setText("");
        }
        else{
            patternID = Resources.fifoSavedRecipes.get(0).toString();
            button3.setText(patternID);
        }
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingPatternActivity.this, RecipeActivity.class);
                Resources.existingID = "1";
                startActivity(intent);
            }
        });

        // Middle button
        if (Resources.fifoSavedRecipes.size() < 2){
            button2.setText("");
        }
        else{
            patternID = Resources.fifoSavedRecipes.get(1).toString();
            button2.setText(patternID);
        }
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingPatternActivity.this, RecipeActivity.class);
                Resources.existingID = "2";
                startActivity(intent);
            }
        });

        // Bottom button
        if (Resources.fifoSavedRecipes.size() < 3){
            button1.setText("");
        }
        else{
            patternID = Resources.fifoSavedRecipes.get(2).toString();
            button1.setText(patternID);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingPatternActivity.this, RecipeActivity.class);
                Resources.existingID = "3";
                startActivity(intent);
            }
        });

        // Back button
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
