package com.example.strikkeapp.app.models;

import com.example.strikkeapp.app.Resources;

import java.util.ArrayList;
import sheep.math.Vector2;

/**
 * Created by oda on 03.06.15.
 */
public class RecipeModel extends SimpleObservable <RecipeModel> {

    public ArrayList<ArrayList<TileModel>> tiles = new ArrayList<ArrayList<TileModel>>();
    int screenWidth;
    int circumference;
    int stitches;
    int numCasts;
    public BoardModel bModel;
    public int columns = Resources.cols*2;

    // CONSTRUCTOR
    public RecipeModel(BoardModel bModel, int circumference, int stitches) {
        this.circumference = circumference;
        this.stitches = stitches;
        this.screenWidth = Resources.screenWidth;
        this.bModel = bModel;

        createTiles(bModel.cropPatternBasedOnTouchedTiles().size(), columns-calculateCropLength());
        generateBorder(bModel.cropPatternBasedOnTouchedTiles());

        calculateCasts();
    }

    public int calculateCropLength(){
        return columns - ((getNumOfMultiplePatternsInBord(bModel.cropPatternBasedOnTouchedTiles())*bModel.cropPatternBasedOnTouchedTiles().get(0).size()));
    }

    // Add tiles to the board
    public ArrayList<ArrayList<TileModel>> createTiles(int rows, int columns){
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

    // Add multiple patterns after each other to create a border
    public void generateBorder(ArrayList<ArrayList<TileModel>> pattern){
        int num = getNumOfMultiplePatternsInBord(pattern);
       // System.out.println("Pattern Rows: " + pattern.size() + "Column: " + pattern.get(0).size() + ", Num: "+num);
        for (int col = 0; col < num ; col++){
            printArray(pattern);
            insertPattern(0, col* pattern.get(0).size(), pattern);
        }
    }

    // Add pattern to the tiles
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
                //System.out.print(state);
            }
            //System.out.println(" ");
        }
        //System.out.println(" --- ");
    }

    public int getNumOfMultiplePatternsInBord(ArrayList<ArrayList<TileModel>> pattern){
        int tileSize = pattern.get(0).size();
        return (columns/tileSize);
    }

    public int calculateCasts(){
        System.out.println("masker: " + numCasts);
        return  numCasts;
    }


}
