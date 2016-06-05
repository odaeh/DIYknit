package com.example.strikkeapp.app.views;

import com.example.strikkeapp.app.activities.DrawActivity;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.OnChangeListener;
import com.example.strikkeapp.app.models.SquareModel;
import com.example.strikkeapp.app.models.SquareState;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import sheep.game.State;
import sheep.math.Vector2;

/**
 * Created by oda on 02.06.15.
 */
public class DrawBoardView extends State implements OnChangeListener<BoardModel> {

    private DrawActivity activity;
    private float screenWidth;
    private BoardModel board;
    public int squareSize;
    private boolean doneInitializing = false;
    private ArrayList<ArrayList<SquareModel>> squares;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;

    // CONSTRUCTOR
    public DrawBoardView(BoardModel board, int screenWidth, DrawActivity activity) {
        this.board = board;
        this.board.addListener(this);
        this.activity = activity;
        this.screenWidth = screenWidth;

        initializeSquares();
        doneInitializing = true;
        board.squareSize = squareSize;
    }

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

    public void calculateSquareSize(){
        int rows = board.getRows();
        squareSize = (int) (screenWidth / rows);
    }

  //--------------------------------------------------------------------------
  // CHANGE LISTENER METHODS:
  //--------------------------------------------------------------------------
    public void onChange(BoardModel board) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                SquareModel square = squares.get(i).get(j);
                square.setSquareState(board.getSquareState(i, j));
            }
        }
    }

    public void update(float dt){
        super.update(dt);
        if (!doneInitializing) return;
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                squares.get(i).get(j).update(dt);
            }
        }
    }
    //--------------------------------------------------------------------------

    public void draw (Canvas canvas){
        if (canvas == null) return;
        canvas.drawColor(Color.rgb(151, 177, 174));
        if (!doneInitializing || canvas == null) return;
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                squares.get(i).get(j).draw(canvas);
            }
        }
    }

    public boolean onTouchDown(MotionEvent touch) {
        int action = touch.getAction();
        int x = 0;
        int y = 0;
        switch(action & MotionEvent.ACTION_MASK) {
           case MotionEvent.ACTION_DOWN:
                y = (int) touch.getY();
                x = (int) touch.getX();

                int col = x / squareSize;
                int row = y / squareSize;
                board.changeSquareState(row, col);
                isOnClick = true;
                break;

           // DOES NOT DETECT MOTION... ONLY TOUCH DOWN
            case MotionEvent.ACTION_MOVE:
                if (isOnClick && (Math.abs(x - touch.getX()) > SCROLL_THRESHOLD || Math.abs(y - touch.getY()) > SCROLL_THRESHOLD)) {
                    System.out.println("movement detected");
                    isOnClick = false;
                }
                break;
        }
        return true;

    }

    // return the final states of all the squares on the grid
    public ArrayList<ArrayList<SquareModel>> sendFinishedPattern(){
        ArrayList<ArrayList<SquareModel>> pattern = new ArrayList<ArrayList<SquareModel>>();
        for (int i = 0; i < squares.size(); i++){
            ArrayList<SquareModel> row = new ArrayList();
            for (int j = 0; j < squares.get(i).size(); j++){
                SquareModel state = squares.get(i).get(j);
                row.add(state);
            }
            pattern.add(row);
        }
        return pattern;
    }
}
