package edu.cs4730.jobintentservicedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * All this fragment does is take in a number and start up the jobIntent service to complete the work.
 */

public class MainActivity extends AppCompatActivity {
    EditText et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input = findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MyJobIntentService.class);  //is any of that needed?  idk.
                //note, putExtra remembers type and I need this to be an integer.  so get an integer first.
                i.putExtra("times", Integer.parseInt(et_input.getText().toString()));  //should do error checking here!
                MyJobIntentService.enqueueWork(getApplicationContext(), i);
            }
        });
    }
}
