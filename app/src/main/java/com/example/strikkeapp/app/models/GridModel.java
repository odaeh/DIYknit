package com.example.strikkeapp.app.models;

import java.util.ArrayList;

/**
 * Created by oda on 03.06.15.
 */
public class GridModel{

    public ArrayList<ArrayList<TileState>> grid;
    final int rows;
    final int cols;

    // CONSTRUCTOR
    public GridModel(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        initGrid();
    }

    // all the tiles of the grid are filled with a state = empty
    private void initGrid(){
        ArrayList<ArrayList<TileState>> grid = new ArrayList();
        for (int i = 0; i < rows; i++){
            ArrayList<TileState> row = new ArrayList();
            for (int j = 0; j < cols; j++){
                row.add(TileState.EMPTY);
            }
            grid.add(row);
        }
        this.grid = grid;
    }

    public TileState getTileState(int row, int col){
        if( row >= getRows() || col >= getCols()){
            return TileState.EMPTY;
        }
        else {
            return grid.get(row).get(col);
        }
    }

    public void setTileState(int row, int col, TileState state){
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
