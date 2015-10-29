package com.example.strikkeapp.app.models;

import java.io.Serializable;

/**
 * Created by oda on 02.06.15.
 */
public class DrawingModel extends SimpleObservable<DrawingModel>{

    public int id;
    private int rows = 15;
    private int cols = 15;

    // CONSTRUCTOR
    public DrawingModel(int id){
        this.id = id;
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }
}
