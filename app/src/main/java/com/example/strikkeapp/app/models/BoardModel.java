package com.example.strikkeapp.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.activities.RecipeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import sheep.math.Vector2;

/**
 * Created by oda on 03.06.15.
 */
public class BoardModel extends SimpleObservable<BoardModel> implements Parcelable{

    private GridModel grid;
    private ArrayList<ArrayList<SquareModel>> patternSquares;
    ArrayList<ArrayList<SquareModel>> croppedPatternSquares;
    public boolean isFinished = false;
    public int squareSize;
    private int[] actualPatternDim = new int[4]; // Left, Right, Top, Bottom
    public int numOfSquaresInBoardWidth;
    public int numOfSquaresInBoardHeight;
    int rows;
    int cols;
    public static int [] patternValues;
    public boolean noneTouchedSquaresOnScreen;

    // CONSTRUCTOR
    public BoardModel(int rows, int cols){
        noneTouchedSquaresOnScreen = false;
        this.rows = rows;
        this.cols = cols;
        initGrid(rows, cols);
    }

    // CONSTRUCTOR USED WHEN RECEIVING DATA
    public BoardModel(Parcel source) {
        int sizeOfPattern = source.readInt();
        squareSize = source.readInt();
        numOfSquaresInBoardWidth = source.readInt();

        this.patternValues = new int[sizeOfPattern * sizeOfPattern];
        source.readIntArray(patternValues);

        if (Resources.existingButtonPushed == true) {
            try {
                patternSquares = buildPatternFromList(sizeOfPattern, RecipeActivity.readStoredFile());
            } catch (Throwable t) {
                System.out.println("Exception: " + t.toString());
            }
        }
        else {
            patternSquares = buildPatternFromList(sizeOfPattern, patternValues);
        }
    }

    // BUILDING PATTERN FROM LIST OF VALUES
    public ArrayList<ArrayList<SquareModel>> buildPatternFromList(int size, int[] patternValues){
        Vector2 sizeVec = new Vector2(squareSize, squareSize); // x and y direction => rectangular squares
        patternSquares = new ArrayList<ArrayList<SquareModel>>();
        for (int i = 0; i < size; i++) {
            patternSquares.add(new ArrayList<SquareModel>());
            for (int j = 0; j < size; j++) {
                Vector2 pos = new Vector2(squareSize * j, squareSize * i);
                SquareModel square = new SquareModel(pos, sizeVec);
                square.setSize(squareSize);
                patternSquares.get(i).add(square);

                if (patternValues[i*size + j] == 1) {
                    patternSquares.get(i).get(j).setSquareState(SquareState.FULL);

                } else if (patternValues[i*size + j] == 0) {
                    patternSquares.get(i).get(j).setSquareState(SquareState.EMPTY);
                }
            }
        }
        return patternSquares;
    }

    // BOARD MODEL CLASS IS PARCELABLE
    public int describeContents() {
        return ((Parcelable)this).hashCode();
    }

    // CHANGE THE SQUARE STATE AND NOTIFY THE DRAW MODEL
    public void changeSquareState(int row, int col){
        if (getSquareState(row, col) == SquareState.EMPTY) {
            setSquareState(row, col, SquareState.FULL);
        }
        else if (getSquareState(row, col) == SquareState.FULL) {
            setSquareState(row, col, SquareState.EMPTY);
        }
        notifyObservers(this);
    }

    public void receivePattern(ArrayList<ArrayList<SquareModel>> pattern){
        this.patternSquares = pattern;
    }

    // CROPS PATTERN BASED ON OUTLINE OF BLACK SQUARES
    public ArrayList<ArrayList<SquareModel>> cropPattern() {
        getSizeOfPattern();
        croppedPatternSquares = new ArrayList<ArrayList<SquareModel>>();
        for (int i = 0; i < patternSquares.size(); i++) {
            ArrayList<SquareModel> newSquareRow = new ArrayList<SquareModel>();
            for (int j = 0; j < patternSquares.get(i).size(); j++) {
                if (getXposOfSquare(patternSquares, i, j) >= actualPatternDim[0] && patternSquares.get(i).get(j).getPosition().getX() <= actualPatternDim[1] && patternSquares.get(i).get(j).getPosition().getY() >= actualPatternDim[2] && patternSquares.get(i).get(j).getPosition().getY() <= actualPatternDim[3]) {
                    newSquareRow.add(patternSquares.get(i).get(j));
                }
            }
            if(newSquareRow.size() > 0) {
                croppedPatternSquares.add(newSquareRow);
            }
        }
        return croppedPatternSquares;
    }

    // GET THE HORIZONTAL POSITION OF A SQUARE
    public float getXposOfSquare(ArrayList<ArrayList<SquareModel>> pattern, int i, int j){
        return pattern.get(i).get(j).getPosition().getX();
    }


    public void getSizeOfPattern() {
        if (patternSquares.size() == 0) {
            noneTouchedSquaresOnScreen = true;
        } else {
            noneTouchedSquaresOnScreen = false;
            ArrayList<Integer> horizontal = new ArrayList<Integer>();
            ArrayList<Integer> vertical = new ArrayList<Integer>();

            for (int i = 0; i < patternSquares.size(); i++) {
                for (int j = 0; j < patternSquares.size(); j++) {
                    SquareState state = patternSquares.get(i).get(j).getSquareState();

                    if (state == SquareState.FULL) {
                        horizontal.add((int) patternSquares.get(i).get(j).getPosition().getX());
                        vertical.add((int) patternSquares.get(i).get(j).getPosition().getY());
                    }
                }
            }
            Collections.sort(horizontal);
            actualPatternDim[0] = horizontal.get(0); // Left
            actualPatternDim[1] = horizontal.get(horizontal.size() - 1); // Right
            Collections.sort(vertical);
            actualPatternDim[2] = vertical.get(0); // Top
            actualPatternDim[3] = vertical.get(vertical.size() - 1); // Bottom

            numOfSquaresInBoardWidth = ((actualPatternDim[1] - actualPatternDim[0]) / squareSize + 1);
            numOfSquaresInBoardHeight = ((actualPatternDim[3] - actualPatternDim[2]) / squareSize + 1);
        }
    }


    // USED WHEN SENDING DATA
    public void writeToParcel(Parcel destination, int flags){
        destination.writeInt(patternSquares.size());
        destination.writeInt(squareSize);
        destination.writeInt(numOfSquaresInBoardHeight);
        destination.writeIntArray(getPatternAsList());
    }

    // CONVERT PATTERN AS A LIST OF SQUARES TO A LIST OF INTEGERS
    public int[] getPatternAsList() {
        int[] patternValues = new int[patternSquares.size() * patternSquares.size()];
        for (int i = 0; i < patternSquares.size(); i++) {
            for (int j = 0; j < patternSquares.get(i).size(); j++) {
                patternValues[i * patternSquares.size() + j] = patternSquares.get(i).get(j).getSquareState().value;
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

    // MAKES A GRID OF SIZE ROWS*COLS
    public void initGrid(int rows, int columns){
        grid = new GridModel(rows, columns);
    }

    // GETERS AND SETERS
    //------------------------------------------------------------
    public int getRows() {
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public SquareState getSquareState (int row, int col){
        return grid.getSquareState(row, col);
    }

    public void setSquareState (int row, int col, SquareState state){
        grid.setSquareState(row, col, state);
    }
    //------------------------------------------------------------


}
