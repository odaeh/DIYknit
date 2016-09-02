package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;

/**
 * Created by oda on 23.07.15.
 */
public class MakeNewPatternActivity extends Activity {

    private EditText field1;
    private EditText field2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_pattern);

        field1 = (EditText) findViewById(R.id.circumference);
        field2 = (EditText) findViewById(R.id.stitch);
        button = (Button) findViewById(R.id.next);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = new Resources();
                boolean ready1;
                boolean ready2;

                if (field1.getText().toString().equals("")){
                    ready1 = false;
                }

                else {
                    res.circumference = field1.getText().toString();
                    ready1 = true;
                }

                if (field2.getText().toString().equals("")){
                    ready2 = false;
                }
                else {
                    res.stitches = field2.getText().toString();
                    ready2 = true;
                }

                if (ready1 && ready2) {
                    // TODO: stitches cannot be larger than circumferrence! Show a message if this happens.
                    Intent intent = new Intent(MakeNewPatternActivity.this, DrawActivity.class);
                    startActivity(intent);
                }
                else{
                    giveMessage();
                }
            }
        });
    }
    public void giveMessage(){
        Toast.makeText(this, "Vennligst skriv inn både omkrets og strikkefasthet på garn!", Toast.LENGTH_LONG).show();
    }
}
