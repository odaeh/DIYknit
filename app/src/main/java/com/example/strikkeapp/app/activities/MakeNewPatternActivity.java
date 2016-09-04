package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;

/**
 * Created by oda on 23.07.15.
 */
public class MakeNewPatternActivity extends Activity {

    private EditText circumferenceField;
    private EditText stitchesField;
    private Button okButton;
    private boolean isCircumferrenceAdded;
    private boolean isStichesAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_pattern);

        circumferenceField = (EditText) findViewById(R.id.circumference);
        stitchesField = (EditText) findViewById(R.id.stitch);
        okButton = (Button) findViewById(R.id.next);

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputFields();

                if (isCircumferrenceAdded && isStichesAdded) {
                    Intent intent = new Intent(MakeNewPatternActivity.this, DrawActivity.class);
                    startActivity(intent);
                }
                else{
                    showErrorMessage();
                }
            }
        });
    }

    private void validateInputFields() {
        if (circumferenceField.getText().toString().equals("")){
            isCircumferrenceAdded = false;
        }

        else {
            Resources.circumference = Integer.parseInt(circumferenceField.getText().toString());
            isCircumferrenceAdded = true;
        }

        if (stitchesField.getText().toString().equals("")){
            isStichesAdded = false;
        }
        else {
            Resources.stitches = Integer.parseInt(stitchesField.getText().toString());
            isStichesAdded = true;
        }
    }

    public void showErrorMessage(){
        Toast.makeText(this, "Vennligst skriv inn både omkrets og strikkefasthet på garn!", Toast.LENGTH_LONG).show();
    }
}
