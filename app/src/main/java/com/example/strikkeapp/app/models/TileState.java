package com.example.strikkeapp.app.models;

/**
 * Created by oda on 03.06.15.
 */
public enum TileState {
    EMPTY(0),
    FULL(1);

    int value;

    private TileState(int value){
        this.value = value;
    }
}
