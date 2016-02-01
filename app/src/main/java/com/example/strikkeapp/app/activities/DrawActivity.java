package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.DrawingModel;
import com.example.strikkeapp.app.views.DrawBoardView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import sheep.game.Game;

public class DrawActivity extends Activity {

    private DrawingModel drawing;
    private BoardModel board;
    private Button button; // to be used to save a pattern
    private int drawingID; // to be used to distinguish between several stored patterns
    private TextView text;
    private String patternID = "";
    final Context context = this;
    public static String storePattern = "storePattern.txt";


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

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.drawView);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, width+30));
        linearLayout.addView(game);

        text = (TextView) findViewById(R.id.title);
        LinearLayout layout= (LinearLayout) findViewById(R.id.info);

        button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pop-up to name the pattern
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Name your pattern");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK" , new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick (DialogInterface dialog, int which){

                        // Send the pattern to generate a recipe
                        Intent intent = new Intent (DrawActivity.this, RecipeActivity.class);
                        intent.putExtra("boardmodel", board);
                        intent.putExtra("patternID" , patternID);

                        // Store patternID in stack in Resources
                        patternID = input.getText().toString();
                        if (Resources.fifo.size() >= 3){
                            Resources.fifo.removeFirst();
                        }
                        Resources.fifo.add(patternID); // adding to the end of the list
                        Resources.pattern = storePattern;
                        saveClicked(board);

                        board.isFinished = true;
                        board.receivePattern(view.sendFinishedPattern());
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void saveClicked(BoardModel bModel) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(storePattern, 0));
            int[] pattern = bModel.getPatternAsList();
            out.write(patternID);
            for (int i = 0; i < pattern.length; i++) {
                out.write(i);
            }
            System.out.println("Filen er lagret!");
            out.close();
        } catch (Throwable t) {

        }
    }
}