package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;

public class MainMenuActivity extends Activity {

    private Button newPatternButton;
    private Button existingPatternButton;
    private Button userGuideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        setContentView(R.layout.main_menu);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, width));


        newPatternButton = (Button) findViewById(R.id.newPattern);
        newPatternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MakeNewPatternActivity.class);
                startActivity(intent);
            }
        });

        existingPatternButton = (Button) findViewById(R.id.existing);
        existingPatternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources.existingButtonPushed = true;
                Intent intent = new Intent(MainMenuActivity.this, ExistingPatternActivity.class);
                startActivity(intent);
            }
        });

        userGuideButton = (Button) findViewById(R.id.guide);
        userGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Linke til en akrivitet med bilder og text over hvordan appen virker
            }
        });
    }

}
