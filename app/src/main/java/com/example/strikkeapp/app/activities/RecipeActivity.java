package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.RecipeModel;
import com.example.strikkeapp.app.views.RecipeView;
import sheep.game.Game;

public class RecipeActivity extends Activity {

    private RecipeModel recipe;
    private int drawingID;
    public int circumference;
    public int stitches;
    public int rows;

    private TextView recipeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);
        setViewElements();

        Game game = new Game(this, null);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        // Receiving the board from the DrawActivity
        Intent intent = getIntent();
        BoardModel bModel = intent.getParcelableExtra("boardmodel");
        circumference = Integer.parseInt(intent.getExtras().getString("circumference"));
        stitches = Integer.parseInt(intent.getExtras().getString("stitches"));
        rows = Integer.parseInt(intent.getExtras().getString("rows"));

        RecipeModel recipe = new RecipeModel(bModel.getPattern(), circumference, stitches, rows, width);
        RecipeView view = new RecipeView(recipe, this, display);
        game.pushState(view);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.recipeView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width/2);
            params.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(params);
        linearLayout.addView(game);

        setRecipeText();
    }

    private void setViewElements() {
        recipeText = (TextView) findViewById(R.id.recipeText);
        recipeText.setText("Legg opp ");
    }

    private void setRecipeText(){
       int numCasts = (circumference / 10); //10 stitches pr cm
       recipeText.setText("Legg opp " + numCasts + " masker.");
    }
}
