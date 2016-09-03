package com.example.strikkeapp.app;

/**
 * Created by oda on 31.01.16.
 */

import com.example.strikkeapp.app.models.BoardModel;

import java.util.LinkedList;

import sheep.math.Vector2;

public class Resources {

    public static  String circumference = "0";
    public static  String stitches = "0";
    public static  LinkedList<String> fifoSavedRecipes = new LinkedList<String>();
    public static  Boolean existingButtonPushed = false;
    public static  Boolean fixNewlyMadePattern = false;
    public static  String[] storedPatternsAsStrings = new String[5];
    public static int rows = 15;
    public static int cols = 15;
    public static String recipeName ="";
    public static int screenWidth;
    public static int screenHeight;
    public static int tileSize;
    public static boolean isPatternEmpty = true;


}
