package com.example.strikkeapp.app.models;

import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;

import sheep.game.Sprite;
import sheep.graphics.Image;
import sheep.math.Vector2;
import android.graphics.Canvas;


/**
 * Created by oda on 02.06.15.
 */
public class TileModel extends Sprite{

    private static Image background = new Image(R.drawable.empty);
    private static Image pattern = new Image(R.drawable.full);
    private TileState state = TileState.EMPTY;
    private Vector2 size;
    private Image currentImage = background;
    public float scaleFactor  = 0.9f;

    //CONSTRUCTOR
    public TileModel(Vector2 pos, Vector2 size){
        super(background);
        setPosition(pos);
        this.size = size;
    }

    // draw the tiles on the canvas with suitable size and image
    @Override
    public void draw (Canvas canvas){
        if (canvas == null) return;
        setSize(size.getX());
        switch (state){
            case EMPTY:
                setImage (background);
                break;
            case FULL:
                setImage (pattern);
        }
        super.draw(canvas);
    }

    // scale the size of the image to fit the square
    public void setSize(float size) {
        float w = currentImage.getWidth();
        float scale = size / w * scaleFactor;
        super.setOffset(0, 0);
        super.setScale(scale, scale);
    }

    // change the current image of the square
    private void setImage(Image image) {
        setView(image); // method from the class Sprite
        currentImage = image;
    }

    // update the tiles continuously
    @Override
    public void update(float dt) {
        super.update(dt);
    }

    // change the state of the square
    public void setTileState(TileState state) {
        this.state = state;
    }

    public TileState getTileState(){
        return this.state;
    }

    public int getTileSize(){
        return (int)size.getX();
    }
}

