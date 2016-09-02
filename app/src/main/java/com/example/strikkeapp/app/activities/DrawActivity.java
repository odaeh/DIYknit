package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.CustomAdapter;
import com.example.strikkeapp.app.views.DrawBoardView;
import sheep.game.Game;

public class DrawActivity extends Activity {

    private BoardModel board;
    private Button saveButton;
    private Button helpButton;
    private TextView title;
    private String patternID = "";
    final Context context = this;
    public int[] patternAsList;
    int rows = Resources.rows;
    int cols = Resources.cols;
    public CustomAdapter adapter;
    int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDisplayMetrics();

        Game patternModule = new Game(this, null); // From external library.

        board = new BoardModel(rows, cols);
        final DrawBoardView drawBoardView = new DrawBoardView(board, this);
        patternModule.pushState(drawBoardView);
        setContentView(R.layout.draw_pattern_screen);

        AlertDialog.Builder userInstructions = new AlertDialog.Builder(context);
        createUserHelpDialog(userInstructions);

        createHelpButton();
        createLayoutElements(patternModule);
        createSaveButton(drawBoardView);
    }

    private void getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        Resources.screenWidth = screenWidth;
        Resources.screenHeight = displayMetrics.heightPixels;
    }

    private void createUserHelpDialog(AlertDialog.Builder instructionsHelp) {
        instructionsHelp.setTitle("Slik lager du ditt eget mønster:");
        instructionsHelp.setMessage("Trykk på rutene for å lage mønster. \n \n  " +
                "Dersom du dobbelklikker vil en farget rute bli tom igjen. \n \n " +
                "Lagre mønsteret når du er ferdig for å få oppskriften.");
        instructionsHelp.setNegativeButton("Lukk", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        instructionsHelp.show();
    }
    public void createHelpButton(){
        helpButton = (Button)  findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder instructionsHelp = new AlertDialog.Builder(context);
                createUserHelpDialog(instructionsHelp);
            }
        });
    }

    private void createLayoutElements(Game patternModule) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.drawPatternModule);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenWidth+board.tileSize));
        linearLayout.addView(patternModule);

        title = (TextView) findViewById(R.id.title);
    }

    public void createSaveButton(final DrawBoardView drawBoardView){
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (board.noTouchedSquaresOnScreen) { // no left square i.e no tiles have been touched adn pattern is empty
                    showMessage();
                    // TODO: Not allowed to save board if none of the tiles are touched i.e there is no pattern
                } else {
                    AlertDialog.Builder nameSavedPattern = new AlertDialog.Builder(context);
                    createGiveNameDialog(nameSavedPattern, drawBoardView);
                }
            }
        });
    }
    public void showMessage() {
        Toast.makeText(this, "Rutenettet er tomt. Vennligst tegn et mønster.", Toast.LENGTH_LONG).show();
    }

    public void createGiveNameDialog(AlertDialog.Builder nameSavedPattern, final DrawBoardView drawBoardView){
        nameSavedPattern.setTitle("Name your pattern: ");
        final EditText nameOfPattern = new EditText(context);
        nameOfPattern.setInputType(InputType.TYPE_CLASS_TEXT);
        nameSavedPattern.setView(nameOfPattern);
        Resources.recipeName = nameOfPattern.getText().toString();
        nameSavedPattern.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                System.out.println("NAVN: " + Resources.recipeName);
                board.isFinishedDrawing = true;
                board.receiveAllTilesOnBoard(drawBoardView.sendFinishedPattern());

                writePatternToFile(board);

                // Send the pattern to generate a recipe
                Intent intent = new Intent(DrawActivity.this, RecipeActivity.class);
                intent.putExtra("boardModel", board);
                intent.putExtra("patternID", patternID);

                // Store patternID in stack in Resources
                patternID = nameOfPattern.getText().toString();
                if (Resources.fifoSavedRecipes.size() >= 3) {
                    Resources.fifoSavedRecipes.removeFirst();
                }
                Resources.fifoSavedRecipes.add(patternID); // adding to the end of the list

                startActivity(intent);
            }
        });
        nameSavedPattern.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        nameSavedPattern.show();
    }

    private void writePatternToFile(BoardModel bModel) {
        patternAsList = bModel.getPatternAsIntArray();
        try{
            write(patternAsList);
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception HER: "+t.toString(), Toast.LENGTH_LONG).show();
            System.out.println("ERROR: " + t);
        }

    }

    public void write (int[] pattern) throws IOException{
        // TODO: save patterns as an int array when the "save" button has been pushed.
        // TODO: store patterns in another list that can be retrieved from the activity "Existing patterns"
        String savedPattern = makeString(board.getPatternAsIntArray());
        FileOutputStream fOut = openFileOutput("test",MODE_WORLD_READABLE);
        fOut.write(savedPattern.getBytes());
        fOut.close();

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

    }
    public static String makeString(int[] intArray){
        String str = "";
        for (int i = 0; i < intArray.length ; i++){
            str = str + intArray[i];
        }
        return str;
    }

    /*
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
    */
}