package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;

public class MainMenuActivity extends Activity {

    private Button newPatternButton;
    private Button existingPatternButton;
    private Button userGuideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDisplayMetrics();
        int width = Resources.screenWidth;

        setContentView(R.layout.main_menu);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, width));

        setLayoutElements();
    }
    private void getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Resources.screenWidth = displayMetrics.widthPixels;
        Resources.screenHeight = displayMetrics.heightPixels;
    }

    private void setLayoutElements() {
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
                // TODO: Link to saved patterns from previous sessions.
            }
        });

        userGuideButton = (Button) findViewById(R.id.guide);
        userGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Link to an activity with a view that shows how the app works (including screenshots).
            }
        });
    }



}
