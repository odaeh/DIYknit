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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import sheep.game.Game;

public class DrawActivity extends Activity {

    private DrawingModel drawing;
    private BoardModel board;
    private Button button; // to be used to save a pattern
    private int drawingID; // to be used to distinguish between several stored patterns
    private TextView text;
    private String patternID = "";
    final Context context = this;
    public int[] pattern;


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

                        saveClicked(board);

                        // Send the pattern to generate a recipe
                        Intent intent = new Intent (DrawActivity.this, RecipeActivity.class);
                        intent.putExtra("boardModel", board);
                        intent.putExtra("patternID" , patternID);

                        // Store patternID in stack in Resources
                        patternID = input.getText().toString();
                        if (Resources.fifo.size() >= 3){
                            Resources.fifo.removeFirst();
                        }
                        Resources.fifo.add(patternID); // adding to the end of the list

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

    // Saving the pattern as a list in a file named "storePattern"
    private void saveClicked(BoardModel bModel) {
        try{
            pattern = bModel.getPatternAsList();
            write(pattern);
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static void write (int[] pattern) throws IOException{
        File file = new File("src\\savedPattern.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < pattern.length; i++){
            bw.write(pattern[i]);
        }
        bw.close();
    }

    private void addStoredPattern(String storedPattern){
        if (Resources.storedPatterns[0].equals("")){
            Resources.storedPatterns[0] = storedPattern;
            System.out.println("HER ER FILEN!!!!!!!");
            System.out.print(Resources.storedPatterns[0]);
        }
        else if (Resources.storedPatterns[1].equals("")){
            Resources.storedPatterns[1] = storedPattern;
            System.out.println("HER ER FILEN!!!!!!!");
            System.out.print(Resources.storedPatterns[1]);

        }
        else if (Resources.storedPatterns[2].equals("")){
            Resources.storedPatterns[2] = storedPattern;
            System.out.println("HER ER FILEN!!!!!!!");
            System.out.print(Resources.storedPatterns[2]);

        }
        else{ // the first pattern is deleted and replaced
            Resources.storedPatterns[0] = storedPattern;
        }
    }
}