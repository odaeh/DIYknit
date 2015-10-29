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
    }

    // the size of the squares depends on the number of squares in a row and the width of the actual screen
    public void calculateSquareSize(){
        int rows = recipe.squares.size();
        squareSize = (int) (width / rows); // width of a square
    }

    // Draw the squares on the canvas
    public void draw (Canvas canvas){
        calculateSquareSize();
        recipe.resizePattern(); // FUNKER IKKE!
        int numPatterns = recipe.getNumMultiplePatterns();
        Vector2 sizeVec = new Vector2(squareSize, squareSize); // rectangular squares
        ArrayList<ArrayList<SquareModel>> squares = new ArrayList<ArrayList<SquareModel>>();

        for (int i = 0; i < recipe.squares.size(); i++) {
            ArrayList<SquareModel> squareRow = new ArrayList<SquareModel>();
            for (int j = 0; j < recipe.squares.get(i).size(); j++) {
                Vector2 pos = new Vector2(squareSize * j, squareSize * i);
                SquareModel square = new SquareModel(pos, sizeVec);
                square.setSize(squareSize);
                squareRow.add(square);
            }
            for (int k = 0; k < numPatterns; k++) {
                squares.add(k, squareRow);
            }
        }
        if (canvas == null) return;
        canvas.drawColor(Color.rgb(151, 177, 174));
        drawSquares(canvas);
    }


    // Draw the square states that is received from the pattern
   private void drawSquares(Canvas canvas) {
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
