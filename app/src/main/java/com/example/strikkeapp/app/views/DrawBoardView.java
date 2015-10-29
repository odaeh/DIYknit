package com.example.strikkeapp.app.views;

import com.example.strikkeapp.app.activities.DrawActivity;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.OnChangeListener;
import com.example.strikkeapp.app.models.SquareModel;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import java.util.ArrayList;
import sheep.game.State;
import sheep.math.Vector2;

/**
 * Created by oda on 02.06.15.
 */
public class DrawBoardView extends State implements OnChangeListener<BoardModel> {

    private DrawActivity activity;
    private float width;
    private BoardModel board;
    private int squareSize;
    private boolean doneInitializing = false;
    private ArrayList<ArrayList<SquareModel>> squares;


    // CONSTRUCTOR
    public DrawBoardView(BoardModel board, Display display, DrawActivity activity) {
        this.board = board;
        this.board.addListener(this);
        this.activity = activity;
        width = display.getWidth();
        initializeSquares();
        doneInitializing = true;
    }

    // when a view is made the squares must be placed on the screen.
    public void initializeSquares() {
        calculateSquareSize();
        Vector2 sizeVec = new Vector2(squareSize, squareSize); // rectangular squares
        ArrayList<ArrayList<SquareModel>> squares = new ArrayList<ArrayList<SquareModel>>();
        for (int i = 0; i < this.board.getRows(); i++) {
            ArrayList<SquareModel> squareRow = new ArrayList<SquareModel>();
            for (int j = 0; j < this.board.getCols(); j++) {
                Vector2 pos = new Vector2(squareSize * j, squareSize * i);
                SquareModel square = new SquareModel(pos, sizeVec);
                square.setSize(squareSize);
                squareRow.add(square);
            }
            squares.add(squareRow);
        }
        this.squares = squares;
    }

    // the size of the squares depends on the number of squares in a row and the width of the actual screen
    public void calculateSquareSize(){
        int rows = board.getRows();
        squareSize = (int) (width / rows); // width of a square
    }

    // whenever a change is made to the View, the BoardModel is notified.
    public void onChange(BoardModel board) {
        updatePattern(board);
    }

    // when a square state is changed in the BoardModel this must be updated in the SquareModel as well
    private void updatePattern(BoardModel board){
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                SquareModel square = squares.get(i).get(j);
                square.setSquareState(board.getSquareState(i, j));
            }
        }
    }

    // the view is updated continuously
    public void update(float dt){
        super.update(dt);
        updateSquares(dt);
    }

    // all the SquareModels on the screen is also updated continuously
    private void updateSquares(float dt){
        if (!doneInitializing) return;
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                squares.get(i).get(j).update(dt);
            }
        }
    }

    // draw the background colour of the canvas, and draw the squares on it
    public void draw (Canvas canvas){
        if (canvas == null) return;
        canvas.drawColor(Color.rgb(151, 177, 174));
        drawSquares(canvas);
    }

    // each square is drawn onto the canvas
    private void drawSquares(Canvas canvas) {
        if (!doneInitializing || canvas == null) return;
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                squares.get(i).get(j).draw(canvas);
            }
        }
    }

    // localise the touch, hence get the square that is touched. This square is then changed.
    public boolean onTouchDown(MotionEvent touch) {
        int y = (int) touch.getY();
        int x = (int) touch.getX();

        int col = x / squareSize;
        int row = y / squareSize;
        board.drawPattern(row, col);
        return true;
    }

    // return the final states of all the squares on the grid
    public ArrayList<ArrayList<SquareModel>> sendFinishedPattern(){
        ArrayList<ArrayList<SquareModel>> pattern = new ArrayList<ArrayList<SquareModel>>();
        for (int i = 0; i < squares.size(); i++){
            ArrayList<SquareModel> row = new ArrayList();
            for (int j = 0; j < squares.get(i).size(); j++){
                SquareModel state = squares.get(i).get(j);
               // Log.v("", "" + state.getSquareState());
                row.add(state);
            }
            pattern.add(row);
        }
        return pattern;
    }
}
