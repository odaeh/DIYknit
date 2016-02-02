package com.example.strikkeapp.app.models;

import java.util.ArrayList;
import java.util.Collections;

import sheep.math.Vector2;

/**
 * Created by oda on 03.06.15.
 */
public class RecipeModel extends SimpleObservable <RecipeModel> {

    String patternID;
    public ArrayList<ArrayList<SquareModel>> squares = new ArrayList<ArrayList<SquareModel>>();
    int screenWidth;
    int circumference;
    int stitches;
    int rows;
    int numCasts;
    public BoardModel bModel;
    int columns = 30;

    // CONSTRUCTOR
    public RecipeModel(BoardModel bModel, int circumference, int stitches, int rows, int screenWidth) {
        this.circumference = circumference;
        this.rows = rows;
        this.stitches = stitches;
        this.screenWidth = screenWidth;
        this.bModel = bModel;
        //this.patternID = patternID;

        createSquares(bModel.getPattern().size(), columns-calculateCropLength());
        generateBorder(bModel.getPattern());

        calculateCasts();
    }

    public int calculateCropLength(){
        return columns - ((getNumMultiplePatterns(bModel.getPattern())*bModel.getPattern().get(0).size()));
    }

    // Add squares to the board
    public ArrayList<ArrayList<SquareModel>> createSquares(int rows, int columns){
        Vector2 sizeVec = new Vector2(bModel.squareSize/2, bModel.squareSize/2); // rectangular square
        for (int i = 0; i < rows; i++) {
            squares.add(new ArrayList<SquareModel>());
            for (int j = 0; j < columns; j++) {
                Vector2 pos = new Vector2(bModel.squareSize * j/2, bModel.squareSize * i/2);
                SquareModel square = new SquareModel(pos, sizeVec);
                square.setSize(bModel.squareSize/2);
                squares.get(i).add(square);
            }
        }
        return squares;
    }

    // Add multiple patterns after each other to create a border
    public void generateBorder(ArrayList<ArrayList<SquareModel>> pattern){
        int num = getNumMultiplePatterns(pattern);
       // System.out.println("Pattern Rows: " + pattern.size() + "Column: " + pattern.get(0).size() + ", Num: "+num);
        for (int col = 0; col < num ; col++){
            printArray(pattern);
            insertPattern(0, col* pattern.get(0).size(), pattern);
        }
    }

    // Add pattern to the squares
    public void insertPattern(int i, int j, ArrayList<ArrayList<SquareModel>> pattern){
        //printArray(squares);
        for (int row = 0; row < pattern.size(); row++){
            for (int col = 0; col < pattern.get(row).size(); col++){
                int squareRow = i + row;
                int squareCol = j + col;
                squares.get(squareRow).get(squareCol).setSquareState(pattern.get(row).get(col).getSquareState());
            }
        }
    }

    public void printArray(ArrayList<ArrayList<SquareModel>> list){
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                char state = list.get(i).get(j).getSquareState() == SquareState.EMPTY ? '.' : '*';
                //System.out.print(state);
            }
            //System.out.println(" ");
        }
        //System.out.println(" --- ");
    }

    public int getNumMultiplePatterns(ArrayList<ArrayList<SquareModel>> pattern){
        return (columns/pattern.get(0).size()); // er plass til 30 ruter bortover da de tegnes i halv størrelse
    }

    public int calculateCasts(){
        System.out.println("masker: " + numCasts);
        return  numCasts;
    }


}
