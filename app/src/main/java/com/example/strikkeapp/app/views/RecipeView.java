package com.example.strikkeapp.app.views;

import android.graphics.Canvas;
import android.graphics.Color;

import com.example.strikkeapp.app.activities.RecipeActivity;
import com.example.strikkeapp.app.models.RecipeModel;
import sheep.game.State;

/**
 * Created by oda on 03.06.15.
 */
public class RecipeView extends State {

    private RecipeModel recipe;
    private RecipeActivity activity;

    // CONSTRUCTOR
    public RecipeView(RecipeModel recipe, RecipeActivity activity){
        this.recipe = recipe;
        this.activity = activity;
    }

    // Draw the tiles on the canvas
    public void draw (Canvas canvas){
        if (canvas == null) return;
        canvas.drawColor(Color.rgb(151, 177, 174));
        for (int i = 0; i < recipe.tiles.size(); i++) {
            for (int j = 0; j < recipe.tiles.get(i).size(); j++) {
                recipe.tiles.get(i).get(j).draw(canvas);
            }
        }
    }

    public void update(float dt){
        super.update(dt);
        updateSquares(dt);
    }

    // all the SquareModels on the screen is also updated continuously
    private void updateSquares(float dt){
        for (int i = 0; i < recipe.tiles.size(); i++) {
            for (int j = 0; j < recipe.tiles.get(i).size() ; j++) {
                recipe.tiles.get(i).get(j).update(dt);
            }
        }
    }
}
