package com.example.strikkeapp.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;

import com.example.strikkeapp.app.activities.DrawActivity;
import com.example.strikkeapp.app.views.DrawBoardView;

import java.util.ArrayList;

import sheep.math.Vector2;

/**
 * Created by oda on 03.06.15.
 */
public class BoardModel extends SimpleObservable<BoardModel> implements OnChangeListener <DrawingModel>, Parcelable{

    private GridModel grid;
    private DrawingModel drawing;
    private ArrayList<ArrayList<SquareModel>> pattern;
    public boolean isFinished = false;
    public int squareSize;

    // CONSTRUCTOR
    public BoardModel(DrawingModel drawing){
        this.drawing = drawing;
        drawing.addListener(this);
        initGrid(drawing);
    }

    public int describeContents() {
        return ((Parcelable)this).hashCode();
    }

    // change the state of a square and notify the DrawingModel
    public void drawPattern(int row, int col){
        if (getSquareState(row, col) == SquareState.EMPTY) {
            setSquareState(row, col, SquareState.FULL);
        }
        else if (getSquareState(row, col) == SquareState.FULL) {
            setSquareState(row, col, SquareState.EMPTY);
        }
        notifyObservers(this);
    }

    // when a change is made, the draw model is notified
    public void onChange(DrawingModel model){
        initGrid(model);
        if (isFinished){// when the user is satisfied with the pattern this is signalised to the DrawingModel
            notifyObservers(this);
        }
    }

    // when a change is made to the board a new grid is made on the drawing
    public void initGrid(DrawingModel model){
        grid = new GridModel(model.getRows(), model.getCols());
    }

    public int getRows() {
        return this.drawing.getRows();
    }

    public int getCols(){
        return this.drawing.getCols();
    }

    public SquareState getSquareState (int row, int col){
        return grid.getSquareState(row, col);
    }

    public void setSquareState (int row, int col, SquareState state){
        grid.setSquareState(row, col, state);
    }

    public void receivePattern(ArrayList<ArrayList<SquareModel>> pattern){
        this.pattern = pattern;
    }
    public ArrayList<ArrayList<SquareModel>> getPattern(){
        return this.pattern;
    }

    // Used when sending data
    public void writeToParcel(Parcel dest, int flags){
        int[] patternValues = new int[pattern.size()*pattern.size()];
        for (int i = 0; i < pattern.size(); i++) {
            for (int j = 0; j < pattern.get(i).size(); j++) {
                patternValues[i*pattern.size()+j] = pattern.get(i).get(j).getSquareState().value;
                squareSize = pattern.get(i).get(j).getSquareSize();
            }
        }
        dest.writeInt(pattern.size());
        dest.writeInt(squareSize);
        dest.writeIntArray(patternValues);
    }

    // Used when receiving data
    public BoardModel(Parcel source) {
        int size = source.readInt();
        int squareSize = (source.readInt() / 2);

        Vector2 sizeVec = new Vector2(squareSize, squareSize); // rectangular squares
        int[] patternValues = new int[size * size];
        source.readIntArray(patternValues);
        pattern = new ArrayList<ArrayList<SquareModel>>();

        // adds a square to the places in the matrix
        for (int i = 0; i < size; i++) {
            pattern.add(new ArrayList<SquareModel>());
            for (int j = 0; j < size; j++) {
                Vector2 pos = new Vector2(squareSize * j, squareSize * i);
                SquareModel square = new SquareModel(pos, sizeVec);
                square.setSize(squareSize);
                pattern.get(i).add(square);

                if (patternValues[i*size + j] == 1) {
                    pattern.get(i).get(j).setSquareState(SquareState.FULL);

                } else if (patternValues[i*size + j] == 0) {
                    pattern.get(i).get(j).setSquareState(SquareState.EMPTY);
                }
            }
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BoardModel createFromParcel(Parcel in) {
            return new BoardModel(in);
        }
        public BoardModel[] newArray(int size) {
            return new BoardModel[size];
        }
    };
}
