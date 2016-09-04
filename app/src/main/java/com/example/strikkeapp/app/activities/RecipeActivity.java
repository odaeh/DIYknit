package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;
import com.example.strikkeapp.app.models.BoardModel;
import com.example.strikkeapp.app.models.RecipeModel;
import com.example.strikkeapp.app.views.RecipeView;

import java.io.IOException;

import sheep.game.Game;

public class RecipeActivity extends Activity {

    public int circumference;
    public int stitches;
    private Button backToMainMenuButton;
    private Button backToDrawingButton;
    private BoardModel bModel;
    private TextView title;
    private TextView recipeText;
    public static String storedPattern;
    public static int[] patternAsList;
    int numCasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);

        Game patternModule = new Game(this, null); // External library.

        // Receiving the board from the DrawActivity:
        Intent intent = getIntent();
        this.bModel = intent.getParcelableExtra("boardModel");
        this.storedPattern = intent.getStringExtra("storedPattern");

        // Retrieving data from Resources:
        this.circumference = Resources.circumference;
        this.stitches = Resources.stitches;

        RecipeModel recipe = new RecipeModel(bModel);
        RecipeView view = new RecipeView(recipe, this);
        patternModule.pushState(view);

        createPatternLayout(patternModule, recipe);
        createLayoutElements();
        setRecipeText();
    }

    private void createPatternLayout(Game patternModule, RecipeModel recipe) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.recipeView);
        float heightOfPatternModule = (bModel.numOfTilesInBoardHeight * Resources.tileSize) / 2;
        int widthOfOnePatternSequence = bModel.numOfTilesInBoardWidth * Resources.tileSize;
        float widthOfPatternModule = ((recipe.columns / bModel.numOfTilesInBoardWidth) * widthOfOnePatternSequence) / 2;

        LinearLayout.LayoutParams patternModuleView = new LinearLayout.LayoutParams((int) widthOfPatternModule, (int) heightOfPatternModule);
        patternModuleView.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(patternModuleView);
        linearLayout.addView(patternModule);
    }

    private void createLayoutElements() {
        title = (TextView) findViewById(R.id.pageTitle);
        title.setText(Resources.recipeName);

        backToMainMenuButton = (Button) findViewById(R.id.backToMainMenuButton);
        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        backToDrawingButton = (Button) findViewById(R.id.backToDrawingButton);
        backToDrawingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Resources.fixNewlyMadePattern = true;
                // TODO: Retrieve the latest pattern when the "back" button is pushed so that changes can be made to the newly made pattern.
                Intent intent = new Intent(RecipeActivity.this, DrawActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRecipeText(){
       recipeText = (TextView) findViewById(R.id.recipeText);
       float numMasks = (circumference * stitches)/10; // stitches pr 10 cm
       int numPatterns = (int)numMasks / bModel.numOfTilesInBoardWidth;
        if ((numMasks - numPatterns*bModel.numOfTilesInBoardWidth) >= (bModel.numOfTilesInBoardWidth / 2)){
            numCasts = (numPatterns+1)*bModel.numOfTilesInBoardWidth;
        }
        else {
            numCasts = numPatterns * bModel.numOfTilesInBoardWidth;
        }
        //System.out.println("Antall masker i omkretsen: " + numPatterns);
        //System.out.println("Antall masker: " + numCasts);
       recipeText.setText("Legg opp " + numCasts + " masker, da vil mønsteret ditt opptre " + numPatterns + " ganger.\n"
               + "Følg så mønster over til du er ferdig med borden. Fortsett så på original oppskrift for genser, lue eller skjerf. " );
    }

    public static int[] readStoredFile() throws IOException {
        //FileInputStream fin = openFileInput(file);
        int c;
        String temp="";
        //while( (c = fin.read()) != -1){
          //  temp = temp + Character.toString((char)c);
       // }
        //fin.close();

        for(int i = 0; i < temp.length() ; i++){
            //int[] list =
        }


        /*
        BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/a directory/" + "a file"));
        String line;
        int counter = 0;
        while ((line = br.readLine()) != null) {
            patternAsList[counter] = Integer.parseInt(line);
            counter++;
        }
        br.close();
        */
        return patternAsList;
    }
}

