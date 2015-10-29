package com.example.strikkeapp.app.models;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oda on 03.06.15.
 */
public class RecipeModel extends SimpleObservable <RecipeModel> {

    int patternId;
    public ArrayList<ArrayList<SquareModel>> squares;
    int width;
    int height;
    int screenWidth;
    int squareSize;
    int circumference;
    int stitches;
    int rows;
    int numCasts;
    int[] actualPatternDim = new int[4]; // Left, Right, Top, Bottom

    // CONSTRUCTOR
    public RecipeModel(ArrayList<ArrayList<SquareModel>> squares, int circumference, int stitches, int rows, int screenWidth) {
        this.squares = squares;
        this.circumference = circumference;
        this.rows = rows;
        this.stitches = stitches;
        this.screenWidth = screenWidth;
        printArray();
        getSizeOfPattern();
        //resizePattern();
        calculateCasts();
    }

    public void getSizeOfPattern() {
        ArrayList<Integer> horizontal = new ArrayList<Integer>();
        ArrayList<Integer> vertical = new ArrayList<Integer>();

        for (int i = 0; i < squares.size(); i++) {
            for (int j = 0; j < squares.size(); j++) {
                SquareState state = squares.get(i).get(j).getSquareState();
                squareSize = squares.get(i).get(j).getSquareSize();

                if (state == SquareState.FULL) {
                    horizontal.add((int) squares.get(i).get(j).getPosition().getX());
                    vertical.add((int) squares.get(i).get(j).getPosition().getY());
                }
            }
        }
        Collections.sort(horizontal);
        actualPatternDim[0] = horizontal.get(0); // Left
        actualPatternDim[1] = horizontal.get(horizontal.size() - 1); // Right
        Collections.sort(vertical);
        actualPatternDim[2] = vertical.get(0); // Top
        actualPatternDim[3] = vertical.get(vertical.size() - 1); // Bottom

        width = ((actualPatternDim[1] - actualPatternDim[0]) / squareSize + 1);
        height = ((actualPatternDim[3] - actualPatternDim[2]) / squareSize + 1);
    }

    public void printArray(){
        for (int i = 0; i < squares.size(); i++) {
            for (int j = 0; j < squares.size(); j++) {
                char state = squares.get(i).get(j).getSquareState() == SquareState.EMPTY ? '.' : '*';
                System.out.print(state);
            }
            System.out.println(" ");
        }
    }

    public void resizePattern() {
        ArrayList<ArrayList<SquareModel>> newSquares = new ArrayList<ArrayList<SquareModel>>();

        for (int i = 0; i < squares.size(); i++) {
            ArrayList<SquareModel> newSquareRow = new ArrayList<SquareModel>();
            System.out.println(squares.size()+ " " + squares.get(i).size());
            for (int j = 0; j < squares.get(i).size(); j++) {
                if (getXpos(squares, i, j) >= actualPatternDim[0] && squares.get(i).get(j).getPosition().getX() <= actualPatternDim[1] && squares.get(i).get(j).getPosition().getY() >= actualPatternDim[2] && squares.get(i).get(j).getPosition().getY() <= actualPatternDim[3]) {
                    newSquareRow.add(squares.get(i).get(j));
                }
            }
            newSquares.add(newSquareRow);
           // System.out.println("antall mønster pr rad: " + numMultiplePatterns);
           // System.out.println("størrelse på rad: " + newSquareRow.size());
        }

        this.squares = newSquares;
    }

    public void insertPattern(int i, int j, ArrayList<ArrayList<SquareModel>> pattern){
        //for (int k = 0; k < numMultiplePatterns; k++) {
        //newSquares.add(newSquareRow);
        //}
    }

    public float getXpos(ArrayList<ArrayList<SquareModel>> pattern, int i, int j){
        return pattern.get(i).get(j).getPosition().getX();
    }

    public int calculateCasts(){

        System.out.println("masker: " + numCasts);
        return  numCasts;
    }

    public int getNumMultiplePatterns(){
        return (screenWidth / squareSize) / width; // er plass til 30 ruter bortover da de tegnes i halv størrelse
    }
}
