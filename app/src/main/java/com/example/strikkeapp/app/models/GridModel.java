package com.example.strikkeapp.app.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by oda on 03.06.15.
 */
public class GridModel{

    public ArrayList<ArrayList<SquareState>> grid;
    final int rows;
    final int cols;

    // CONSTRUCTOR
    public GridModel(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        initGrid();
    }

    // all the squares of the grid are filled with a state = empty
    private void initGrid(){
        ArrayList<ArrayList<SquareState>> grid = new ArrayList();
        for (int i = 0; i < rows; i++){
            ArrayList<SquareState> row = new ArrayList();
            for (int j = 0; j < cols; j++){
                row.add(SquareState.EMPTY);
            }
            grid.add(row);
        }
        this.grid = grid;
    }

    public SquareState getSquareState(int row, int col){
        if( row >= getRows() || col >= getCols()){
            return SquareState.EMPTY;
        }
        else {
            return grid.get(row).get(col);
        }
    }

    public void setSquareState(int row, int col, SquareState state){
        if (row >= getRows() || col >= getCols()) {
            return;
        }
        else {
            grid.get(row).set(col, state);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
