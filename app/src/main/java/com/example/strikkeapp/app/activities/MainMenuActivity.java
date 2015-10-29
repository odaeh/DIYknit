package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.strikkeapp.app.R;

public class MainMenuActivity extends Activity {

    private Button newPattern;
    private Button existingPattern;
    private Button userGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        setContentView(R.layout.main_menu);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, width));


        newPattern = (Button) findViewById(R.id.newPattern);
        newPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MakeNewPatternActivity.class);
                startActivity(intent);
            }
        });

        existingPattern = (Button) findViewById(R.id.existing);
        existingPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Linke til en aktivitet med en liste av eksisterende mønstre og hhv. oppskrifter
            }
        });

        userGuide = (Button) findViewById(R.id.guide);
        userGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Linke til en akrivitet med bilder og text over hvordan appen virker
            }
        });

    }
}
