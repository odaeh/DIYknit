package com.example.strikkeapp.app.models;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oda on 03.06.15.
 */
public class RecipeModel extends SimpleObservable <RecipeModel> {

    int patternId;
    public ArrayList<ArrayList<SquareModel>> pattern;
    int width;
    int height;
    int screenWidth;
    int squareSize;
    int circumference;
    int stitches;
    int rows;
    int numCasts;
    int[] actualPatternDim = new int[4]; // Left, Right, Top, Bottom
    int numMultiplePatterns;

    // CONSTRUCTOR
    public RecipeModel(ArrayList<ArrayList<SquareModel>> pattern, int circumference, int stitches, int rows, int screenWidth) {
        this.pattern = pattern;
        this.circumference = circumference;
        this.rows = rows;
        this.stitches = stitches;
        this.screenWidth = screenWidth;
        getSizeOfPattern();
        //resizePattern();
        calculateCasts();
    }

    public void getSizeOfPattern() {
        ArrayList<Integer> horizontal = new ArrayList<Integer>();
        ArrayList<Integer> vertical = new ArrayList<Integer>();

        for (int i = 0; i < pattern.size(); i++) {
            for (int j = 0; j < pattern.size(); j++) {
                SquareState state = pattern.get(i).get(j).getSquareState();
                squareSize = pattern.get(i).get(j).getSquareSize();

                if (state == SquareState.FULL) {
                    horizontal.add((int) pattern.get(i).get(j).getPosition().getX());
                    vertical.add((int) pattern.get(i).get(j).getPosition().getY());
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

    public void resizePattern() {
        ArrayList<ArrayList<SquareModel>> newSquares = new ArrayList<ArrayList<SquareModel>>();

        //numMultiplePatterns = (screenWidth / squareSize) / width; // er plass til 30 ruter bortover da de tegnes i halv størrelse

        for (int i = 0; i < pattern.size(); i++) {
            ArrayList<SquareModel> newSquareRow = new ArrayList<SquareModel>();
            System.out.println(pattern.size()+ " " + pattern.get(i).size());
            for (int j = 0; j < pattern.get(i).size(); j++) {
                if (getXpos(pattern, i, j) >= actualPatternDim[0] && pattern.get(i).get(j).getPosition().getX() <= actualPatternDim[1] && pattern.get(i).get(j).getPosition().getY() >= actualPatternDim[2] && pattern.get(i).get(j).getPosition().getY() <= actualPatternDim[3]) {
                    newSquareRow.add(pattern.get(i).get(j));
                }
            }
            //for (int k = 0; k < numMultiplePatterns; k++) {
                newSquares.add(newSquareRow);
            //}
            System.out.println("antall mønster pr rad: " + numMultiplePatterns);
            System.out.println("størrelse på rad: " + newSquareRow.size());
        }

        this.pattern = newSquares;
    }



    public float getXpos(ArrayList<ArrayList<SquareModel>> pattern, int i, int j){
        return pattern.get(i).get(j).getPosition().getX();
    }

    public int calculateCasts(){

        System.out.println("masker: " + numCasts);
        return  numCasts;
    }

    public int getNumMultiplePatterns(){
        return numMultiplePatterns;
    }
}
