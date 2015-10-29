package com.example.strikkeapp.app.models;

/**
 * Created by oda on 03.06.15.
 */
public enum SquareState {
    EMPTY(0),
    FULL(1);

    int value;

    private SquareState (int value){
        this.value = value;
    }
}
