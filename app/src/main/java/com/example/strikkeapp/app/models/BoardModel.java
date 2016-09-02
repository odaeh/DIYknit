package com.example.strikkeapp.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.activities.RecipeActivity;

import java.util.ArrayList;
import java.util.Collections;
import sheep.math.Vector2;

/**
 * Created by oda on 03.06.15.
 */
public class BoardModel extends SimpleObservable<BoardModel> implements Parcelable{

    private GridModel gridFullOfEmptyTiles;
    private ArrayList<ArrayList<TileModel>> allTiles;
    public ArrayList<ArrayList<TileModel>> tilesInPattern;
    public boolean isFinishedDrawing = false;
    public int tileSize;
    private int[] retrieveDrawnPatternDim = new int[4]; // Left, Right, Top, Bottom
    public int numOfTilesInBoardWidth;
    public int numOfTilesInBoardHeight;
    int rowsOnBoard = Resources.rows;
    int colsOnBoard = Resources.cols;
    public static int [] patternAsIntArray;
    public boolean isPatternEmpty;

    // CONSTRUCTOR
    public BoardModel(){
        this.tileSize = calculateTileSize();
        gridFullOfEmptyTiles = new GridModel(Resources.rows, Resources.cols);
    }

    // CONSTRUCTOR USED WHEN RECEIVING DATA IN RECIPE ACTIVITY
    public BoardModel(Parcel source) {
        int sizeOfPattern = source.readInt();
        numOfTilesInBoardWidth = source.readInt();

        this.patternAsIntArray = new int[sizeOfPattern * sizeOfPattern];
        source.readIntArray(patternAsIntArray);

        if (Resources.existingButtonPushed == true) {
            try {
                allTiles = buildPatternFromIntArray(sizeOfPattern, RecipeActivity.readStoredFile());
            } catch (Throwable t) {
                System.out.println("Exception: " + t.toString());
            }
        }
        else {
            allTiles = buildPatternFromIntArray(sizeOfPattern, patternAsIntArray);
        }
    }

    public int calculateTileSize(){
        tileSize = (int) (Resources.screenWidth / Resources.cols);
        Resources.tileSize = tileSize;
        return tileSize;
    }

    public ArrayList<ArrayList<TileModel>> buildPatternFromIntArray(int size, int[] patternValues){
        Vector2 sizeVec = new Vector2(tileSize, tileSize); // Vector2 is a method from the external library.
        allTiles = new ArrayList<ArrayList<TileModel>>();
        for (int i = 0; i < size; i++) {
            allTiles.add(new ArrayList<TileModel>());
            for (int j = 0; j < size; j++) {
                Vector2 pos = new Vector2(tileSize * j, tileSize * i);
                TileModel tile = new TileModel(pos, sizeVec);
                tile.setSize(tileSize);
                allTiles.get(i).add(tile);

                if (patternValues[i*size + j] == 1) {
                    allTiles.get(i).get(j).setTileState(TileState.FULL);

                } else if (patternValues[i*size + j] == 0) {
                    allTiles.get(i).get(j).setTileState(TileState.EMPTY);
                }
            }
        }
        return allTiles;
    }

    // BOARD MODEL CLASS IS PARCELABLE
    public int describeContents() {
        return ((Parcelable)this).hashCode();
    }

    // CHANGE THE TILE STATE AND NOTIFY THE DRAW MODEL
    public void changeTileState(int row, int col){
        if (getTileState(row, col) == TileState.EMPTY) {
            setTileState(row, col, TileState.FULL);
        }
        else if (getTileState(row, col) == TileState.FULL) {
            setTileState(row, col, TileState.EMPTY);
        }
        notifyObservers(this);
    }

    public void receiveAllTilesOnBoard(ArrayList<ArrayList<TileModel>> allTiles){
        this.allTiles = allTiles;
    }

    public ArrayList<ArrayList<TileModel>> cropPatternBasedOnTouchedTiles() {
        getSizeOfPattern();
        tilesInPattern = new ArrayList<ArrayList<TileModel>>();
        for (int i = 0; i < allTiles.size(); i++) {
            ArrayList<TileModel> rowOfTiles = new ArrayList<TileModel>();
            for (int j = 0; j < allTiles.get(i).size(); j++) {
                if (getXposOfTile(allTiles, i, j) >= retrieveDrawnPatternDim[0] && allTiles.get(i).get(j).getPosition().getX() <= retrieveDrawnPatternDim[1] && allTiles.get(i).get(j).getPosition().getY() >= retrieveDrawnPatternDim[2] && allTiles.get(i).get(j).getPosition().getY() <= retrieveDrawnPatternDim[3]) {
                    rowOfTiles.add(allTiles.get(i).get(j));
                }
            }
            if(rowOfTiles.size() > 0) {
                tilesInPattern.add(rowOfTiles);
            }
        }
        return tilesInPattern;
    }

    // GET THE HORIZONTAL POSITION OF A TILE
    public float getXposOfTile(ArrayList<ArrayList<TileModel>> allTiles, int i, int j){
        return allTiles.get(i).get(j).getPosition().getX();
    }

    public void getSizeOfPattern() {
        if (allTiles.size() == 0) {
            isPatternEmpty = true;
        } else {
            isPatternEmpty = false;
            ArrayList<Integer> tilesHoriziontalDirection = new ArrayList<Integer>();
            ArrayList<Integer> tilesVerticalDirection = new ArrayList<Integer>();

            for (int i = 0; i < allTiles.size(); i++) {
                for (int j = 0; j < allTiles.size(); j++) {
                    TileState state = allTiles.get(i).get(j).getTileState();

                    if (state == TileState.FULL) {
                        tilesHoriziontalDirection.add((int) allTiles.get(i).get(j).getPosition().getX());
                        tilesVerticalDirection.add((int) allTiles.get(i).get(j).getPosition().getY());
                    }
                }
            }
            Collections.sort(tilesHoriziontalDirection);
            retrieveDrawnPatternDim[0] = tilesHoriziontalDirection.get(0); // Left
            retrieveDrawnPatternDim[1] = tilesHoriziontalDirection.get(tilesHoriziontalDirection.size() - 1); // Right
            Collections.sort(tilesVerticalDirection);
            retrieveDrawnPatternDim[2] = tilesVerticalDirection.get(0); // Top
            retrieveDrawnPatternDim[3] = tilesVerticalDirection.get(tilesVerticalDirection.size() - 1); // Bottom

            numOfTilesInBoardWidth = ((retrieveDrawnPatternDim[1] - retrieveDrawnPatternDim[0]) / Resources.tileSize + 1);
            numOfTilesInBoardHeight = ((retrieveDrawnPatternDim[3] - retrieveDrawnPatternDim[2]) / Resources.tileSize + 1);
        }
    }


    // USED WHEN SENDING DATA FROM THE DRAW ACTIVITY
    public void writeToParcel(Parcel destination, int flags){
        destination.writeInt(allTiles.size());
        destination.writeInt(numOfTilesInBoardHeight);
        destination.writeIntArray(getPatternAsIntArray());
    }

    // CONVERT PATTERN AS A LIST OF TILES TO A LIST OF INTEGERS
    public int[] getPatternAsIntArray() {
        int[] patternValues = new int[allTiles.size() * allTiles.size()];
        for (int i = 0; i < allTiles.size(); i++) {
            for (int j = 0; j < allTiles.get(i).size(); j++) {
                patternValues[i * allTiles.size() + j] = allTiles.get(i).get(j).getTileState().value;
            }
        }
        return patternValues;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BoardModel createFromParcel(Parcel in) {
            return new BoardModel(in);
        }
        public BoardModel[] newArray(int size) {
            return new BoardModel[size];
        }
    };



    // GETERS AND SETERS
    //------------------------------------------------------------
    public int getRowsOnBoard() {
        return rowsOnBoard;
    }

    public int getColsOnBoard(){
        return colsOnBoard;
    }

    public TileState getTileState(int row, int col){
        return gridFullOfEmptyTiles.getTileState(row, col);
    }

    public void setTileState(int row, int col, TileState state){
        gridFullOfEmptyTiles.setTileState(row, col, state);

    }
    //------------------------------------------------------------


}
