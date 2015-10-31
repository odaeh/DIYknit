package com.example.strikkeapp.app.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Display;
import com.example.strikkeapp.app.activities.RecipeActivity;
import com.example.strikkeapp.app.models.RecipeModel;
import com.example.strikkeapp.app.models.SquareModel;

import java.util.ArrayList;
import sheep.game.State;
import sheep.math.Vector2;

/**
 * Created by oda on 03.06.15.
 */
public class RecipeView extends State {

    private RecipeModel recipe;
    private int squareSize;
    private float width;
    private RecipeActivity activity;

    // CONSTRUCTOR
    public RecipeView(RecipeModel recipe, RecipeActivity activity, Display display){
        this.recipe = recipe;
        this.activity = activity;
        width = display.getWidth();
        calculateSquareSize();
    }

    // the size of the squares depends on the number of squares in a row and the width of the actual screen
    public void calculateSquareSize(){
        int rows = recipe.squares.size();
        //squareSize = (int) (width / rows)/4; // width of a square

        for (int i = 0; i < recipe.squares.size(); i++) {
            for (int j = 0; j < recipe.squares.get(i).size(); j++) {
                int squareSize = recipe.bModel.squareSize;
               recipe.squares.get(i).get(j).setSize(squareSize / 10);
            }
        }
    }

    // Draw the squares on the canvas
    public void draw (Canvas canvas){
        canvas.drawColor(Color.rgb(151, 177, 174));
        drawSquares(canvas);
    }

    // Draw the square states that is received from the pattern
   private void drawSquares(Canvas canvas) {
       calculateSquareSize();
       for (int i = 0; i < recipe.squares.size(); i++) {
           for (int j = 0; j < recipe.squares.get(i).size(); j++) {
               recipe.squares.get(i).get(j).draw(canvas);
           }
       }
   }

    public void update(float dt){
        super.update(dt);
        updateSquares(dt);
    }

    // all the SquareModels on the screen is also updated continuously
    private void updateSquares(float dt){
        for (int i = 0; i < recipe.squares.size(); i++) {
            for (int j = 0; j < recipe.squares.get(i).size() ; j++) {
                recipe.squares.get(i).get(j).update(dt);
            }
        }
    }
}
