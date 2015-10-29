package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.DrawingModel;
import com.example.strikkeapp.app.views.DrawBoardView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import sheep.game.Game;

public class DrawActivity extends Activity {

    private DrawingModel drawing;
    private BoardModel board;
    private Button button; // to be used to save a pattern
    private int drawingID; // to be used to distinguish between several stored patterns
    private TextView text;
    private String circumference;
    private String stitches;
    private String rows;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Game game = new Game(this, null);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        drawing = new DrawingModel(drawingID);
        board = new BoardModel(drawing);
        final DrawBoardView view = new DrawBoardView(board, display, this);

        game.pushState(view);
        setContentView(R.layout.drawing);

        Bundle extras = getIntent().getExtras();
        circumference = extras.getString("circumference");
        stitches = extras.getString("stitches");
        rows = extras.getString("rows");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.drawView);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, width+30));
        linearLayout.addView(game);

        text = (TextView) findViewById(R.id.title);
       // listView = (ListView) findViewById(R.id.instructions);
        LinearLayout layout= (LinearLayout) findViewById(R.id.info);

        button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
       // when the button is pushed the pattern is saved and a recipe is generated
            public void onClick(View v) {
                Intent intent = new Intent (DrawActivity.this, RecipeActivity.class);
                intent.putExtra("boardmodel", board);
                intent.putExtra("circumference", circumference);
                intent.putExtra("stitches", stitches);
                intent.putExtra("rows", rows);
                board.isFinished = true;
                board.receivePattern(view.sendFinishedPattern());
                startActivity(intent);
            }
        });
    }
}
/*
<ListView
android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/instructions"
        android:text = "Draw a pattern, save it and receive the knitting recipe.">
</ListView>
*/