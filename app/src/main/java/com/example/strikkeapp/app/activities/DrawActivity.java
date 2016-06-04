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
import com.example.strikkeapp.app.views.DrawBoardView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;
import sheep.game.Game;

public class DrawActivity extends Activity {

    private BoardModel board;
    private Button saveButton;
    public int drawingID; // to be used to distinguish between several stored patterns
    private TextView title;
    private String patternID = "";
    final Context context = this;
    public int[] patternList;
    int rows = 20; //it these are changed, the number of columns in RecipeModel must be changed as well!
    int cols = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Game patternModule = new Game(this, null);

        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();

        board = new BoardModel(rows, cols);
        final DrawBoardView drawBoardView = new DrawBoardView(board, screenWidth, this);

        patternModule.pushState(drawBoardView);
        setContentView(R.layout.draw_pattern_screen);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.drawPatternModule);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenWidth+30));
        linearLayout.addView(patternModule);

        title = (TextView) findViewById(R.id.title);
        LinearLayout layout= (LinearLayout) findViewById(R.id.instructions);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder nameOfSavedPattern = new AlertDialog.Builder(context);
                nameOfSavedPattern.setTitle("Name your pattern: ");
                final EditText nameOfPattern = new EditText(context);
                nameOfPattern.setInputType(InputType.TYPE_CLASS_TEXT);
                nameOfSavedPattern.setView(nameOfPattern);
                nameOfSavedPattern.setPositiveButton("OK" , new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick (DialogInterface dialog, int which){

                        board.isFinished = true;
                        board.receivePattern(drawBoardView.sendFinishedPattern());

                        saveClicked(board);

                        // Send the pattern to generate a recipe
                        Intent intent = new Intent (DrawActivity.this, RecipeActivity.class);
                        intent.putExtra("boardModel", board);
                        intent.putExtra("patternID" , patternID);

                        // Store patternID in stack in Resources
                        patternID = nameOfPattern.getText().toString();
                        if (Resources.fifoSavedRecipes.size() >= 3){
                            Resources.fifoSavedRecipes.removeFirst();
                        }
                        Resources.fifoSavedRecipes.add(patternID); // adding to the end of the list

                        startActivity(intent);
                    }
                });
                nameOfSavedPattern.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                nameOfSavedPattern.show();
            }
        });
    }

    // Saving the pattern as a list in a file named "storePattern"
    private void saveClicked(BoardModel bModel) {
        try{
            write(patternList);
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception HER: "+t.toString(), Toast.LENGTH_LONG).show();
            System.out.println("ERROR: " + t);
        }
        patternList = bModel.getPatternAsList();
    }


    public static String makeString(int[] intArray){
        String str = "";
        for (int i = 0; i < intArray.length ; i++){
            str = str + intArray[i];
        }
        return str;
    }

    public void write (int[] pattern) throws IOException{
        String savedPattern = makeString(board.getPatternAsList());
        FileOutputStream fOut = openFileOutput("test",MODE_WORLD_READABLE);
        fOut.write(savedPattern.getBytes());
        fOut.close();

        /*
        File file = new File(Environment.getExternalStorageDirectory() + "/a directory/" + "patternID");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < pattern.length; i++){
            bw.write(pattern[i]);
        }
        bw.close();
        */
    }

    private void addStoredPattern(String storedPattern){
        if (Resources.storedPatternsAsStrings[0].equals("")){
            Resources.storedPatternsAsStrings[0] = storedPattern;
            System.out.println("HER ER FILEN!!!!!!!");
            System.out.print(Resources.storedPatternsAsStrings[0]);
        }
        else if (Resources.storedPatternsAsStrings[1].equals("")){
            Resources.storedPatternsAsStrings[1] = storedPattern;
            System.out.println("HER ER FILEN!!!!!!!");
            System.out.print(Resources.storedPatternsAsStrings[1]);

        }
        else if (Resources.storedPatternsAsStrings[2].equals("")){
            Resources.storedPatternsAsStrings[2] = storedPattern;
            System.out.println("HER ER FILEN!!!!!!!");
            System.out.print(Resources.storedPatternsAsStrings[2]);

        }
        else{ // the first pattern is deleted and replaced
            Resources.storedPatternsAsStrings[0] = storedPattern;
        }
    }
}