package edu.cs4730.jobservicedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et_input = (EditText) findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobService.scheduleJob(MainActivity.this, Integer.valueOf(et_input.getText().toString()), false);  //mainActivity context, not listener...
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobService.scheduleJob(MainActivity.this, Integer.valueOf(et_input.getText().toString()), true);  //mainActivity context, not listener...
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobService.cancelJob(MainActivity.this);
            }
        });

    }
}
