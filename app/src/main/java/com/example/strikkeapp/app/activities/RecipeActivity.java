package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.RecipeModel;
import com.example.strikkeapp.app.views.RecipeView;

import java.io.OutputStreamWriter;
import java.util.ArrayList;

import sheep.game.Game;

public class RecipeActivity extends Activity {

    private RecipeModel recipe;
    public int circumference;
    public int stitches;
    public int rows;
    private Button button;
    private BoardModel bModel;
    private TextView recipeText;
    private String patternID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);

        Game game = new Game(this, null);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        // Receiving the board from the DrawActivity
        Intent intent = getIntent();
        this.bModel = intent.getParcelableExtra("boardmodel");
        this.patternID = intent.getStringExtra("patternID");
        this.circumference = Integer.parseInt(Resources.circumference);
        this.stitches = Integer.parseInt(Resources.stitches);
        this.rows = Integer.parseInt(Resources.rows);

        RecipeModel recipe = new RecipeModel(patternID, bModel, circumference, stitches, rows, width);
        RecipeView view = new RecipeView(recipe, this, display);
        game.pushState(view);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.recipeView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width/2);
            params.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(params);
        linearLayout.addView(game);

        setRecipeText();

        button = (Button) findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    } //OnCreate

    private void setRecipeText(){
       recipeText = (TextView) findViewById(R.id.recipeText);
       int numCasts = (circumference * 10); //10 stitches pr cm
       recipeText.setText("Legg opp " + numCasts + " masker.");
    }
}
