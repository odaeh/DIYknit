package com.example.strikkeapp.app.views;

import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.activities.DrawActivity;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.OnChangeListener;
import com.example.strikkeapp.app.models.TileModel;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import java.util.ArrayList;
import sheep.game.State;
import sheep.math.Vector2;

/**
 * Created by oda on 02.06.15.
 */
public class DrawBoardView extends State implements OnChangeListener<BoardModel> {

    private DrawActivity activity;
    private BoardModel board;
    public int tileSize;
    private boolean doneInitializing = false;
    private ArrayList<ArrayList<TileModel>> tiles;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;

    // CONSTRUCTOR
    public DrawBoardView(BoardModel board, DrawActivity activity) {
        this.board = board;
        this.board.addListener(this);
        this.activity = activity;

        initializeTiles();
        doneInitializing = true;
    }

    public void initializeTiles() {
        this.tileSize = Resources.tileSize;
        Vector2 sizeVec = new Vector2(tileSize, tileSize); // rectangular tiles
        ArrayList<ArrayList<TileModel>> tiles = new ArrayList<ArrayList<TileModel>>();
        for (int i = 0; i < Resources.rows; i++) {
            ArrayList<TileModel> squareRow = new ArrayList<TileModel>();
            for (int j = 0; j < Resources.cols; j++) {
                Vector2 pos = new Vector2(tileSize * j, tileSize * i);
                TileModel square = new TileModel(pos, sizeVec);
                square.setSize(tileSize);
                squareRow.add(square);
            }
            tiles.add(squareRow);
        }
        this.tiles = tiles;
    }



  //--------------------------------------------------------------------------
  // CHANGE LISTENER METHODS:
  //--------------------------------------------------------------------------
    public void onChange(BoardModel board) {
        for (int i = 0; i < board.getRowsOnBoard(); i++) {
            for (int j = 0; j < board.getColsOnBoard(); j++) {
                TileModel square = tiles.get(i).get(j);
                square.setTileState(board.getTileState(i, j));
            }
        }
    }

    public void update(float dt){
        super.update(dt);
        if (!doneInitializing) return;
        for (int i = 0; i < board.getRowsOnBoard(); i++) {
            for (int j = 0; j < board.getColsOnBoard(); j++) {
                tiles.get(i).get(j).update(dt);
            }
        }
    }
    //--------------------------------------------------------------------------

    public void draw (Canvas canvas){
        if (canvas == null) return;
        canvas.drawColor(Color.rgb(151, 177, 174));
        if (!doneInitializing || canvas == null) return;
        for (int i = 0; i < board.getRowsOnBoard(); i++) {
            for (int j = 0; j < board.getColsOnBoard(); j++) {
                tiles.get(i).get(j).draw(canvas);
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

                int col = x / tileSize;
                int row = y / tileSize;
                board.changeTileState(row, col);
                isOnClick = true;
                break;

           // DOES NOT DETECT MOTION... ONLY TOUCH DOWN
            case MotionEvent.ACTION_MOVE:
                if (isOnClick && (Math.abs(x - touch.getX()) > SCROLL_THRESHOLD || Math.abs(y - touch.getY()) > SCROLL_THRESHOLD)) {
                    //System.out.println("movement detected");
                    isOnClick = false;
                }
                break;
        }
        return true;

    }

    // return the final states of all the tiles on the grid
    public ArrayList<ArrayList<TileModel>> sendFinishedPattern(){
        ArrayList<ArrayList<TileModel>> pattern = new ArrayList<ArrayList<TileModel>>();
        for (int i = 0; i < tiles.size(); i++){
            ArrayList<TileModel> row = new ArrayList();
            for (int j = 0; j < tiles.get(i).size(); j++){
                TileModel state = tiles.get(i).get(j);
                row.add(state);
            }
            pattern.add(row);
        }
        return pattern;
    }
}
