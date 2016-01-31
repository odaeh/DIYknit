package com.example.strikkeapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.strikkeapp.app.R;
import com.example.strikkeapp.app.Resources;

/**
 * Created by oda on 23.07.15.
 */
public class MakeNewPatternActivity extends Activity {

    private EditText field1;
    private EditText field2;
    private EditText field3;
    private Button button;
    private String circumference;
    private String stitches;
    private String rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_pattern);

        field1 = (EditText) findViewById(R.id.circumference);
        field2 = (EditText) findViewById(R.id.stitch);
        field3 = (EditText) findViewById(R.id.rows);
        button = (Button) findViewById(R.id.next);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = new Resources();

                if (field1.getText().toString().equals("")){
                    res.circumference = "0";
                }
                else {
                    res.circumference = field1.getText().toString();
                }

                if (field2.getText().toString().equals("")){
                    res.stitches = "0";
                }
                else {
                    res.stitches = field2.getText().toString();
                }

                if (field3.getText().toString().equals("")){
                    res.rows = "0";
                }
                else {
                    res.rows = field3.getText().toString();
                }

                Intent intent = new Intent(MakeNewPatternActivity.this, DrawActivity.class);
                startActivity(intent);
            }
        });
    }
}
