package com.example.strikkeapp.app.models;

import com.example.strikkeapp.app.Resources;
import java.util.ArrayList;
import sheep.math.Vector2;

/**
 * Created by oda on 03.06.15.
 */
public class RecipeModel extends SimpleObservable <RecipeModel> {

    public ArrayList<ArrayList<TileModel>> tiles = new ArrayList<ArrayList<TileModel>>();
    int circumference;
    int stitches;
    int numCasts;
    public BoardModel bModel;
    public int columns;

    // CONSTRUCTOR
    public RecipeModel(BoardModel bModel) {
        this.circumference = Resources.circumference;
        this.stitches = Resources.stitches;
        this.bModel = bModel;
        this.columns = Resources.cols*2;

        ArrayList<ArrayList<TileModel>> croppedPattern = bModel.cropPatternBasedOnTouchedTiles();
        createFinishedPattern(croppedPattern.size(), columns-calculateCropLength());
        generateBorder(croppedPattern);
    }

    public ArrayList<ArrayList<TileModel>> createFinishedPattern(int rows, int columns){
        Vector2 sizeVec = new Vector2(bModel.tileSize /2, bModel.tileSize /2);
        for (int i = 0; i < rows; i++) {
            tiles.add(new ArrayList<TileModel>());
            for (int j = 0; j < columns; j++) {
                Vector2 pos = new Vector2(bModel.tileSize * j/2, bModel.tileSize * i/2);
                TileModel tile = new TileModel(pos, sizeVec);
                tile.setSize(bModel.tileSize /2);
                tiles.get(i).add(tile);
            }
        }
        return tiles;
    }

    public int calculateCropLength(){
        ArrayList<ArrayList<TileModel>> croppedPattern = bModel.cropPatternBasedOnTouchedTiles();
        return columns - (getNumOfMultiplePatternsInBord(croppedPattern) * croppedPattern.get(0).size());
    }

    public void generateBorder(ArrayList<ArrayList<TileModel>> pattern){
        int num = getNumOfMultiplePatternsInBord(pattern);
        for (int col = 0; col < num ; col++){
            //printArray(pattern);
            insertPattern(0, col* pattern.get(0).size(), pattern);
        }
    }

    public int getNumOfMultiplePatternsInBord(ArrayList<ArrayList<TileModel>> pattern){
        int tileSize = pattern.get(0).size();
        return (columns/tileSize);
    }

    public void insertPattern(int i, int j, ArrayList<ArrayList<TileModel>> pattern){
        //printArray(tiles);
        for (int row = 0; row < pattern.size(); row++){
            for (int col = 0; col < pattern.get(row).size(); col++){
                int rowOfTiles = i + row;
                int colOfTiles = j + col;
                tiles.get(rowOfTiles).get(colOfTiles).setTileState(pattern.get(row).get(col).getTileState());
            }
        }
    }

    public void printArray(ArrayList<ArrayList<TileModel>> list){
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                char state = list.get(i).get(j).getTileState() == TileState.EMPTY ? '.' : '*';
            }
        }
    }
}
