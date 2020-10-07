package edu.cs4730.jobservicedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * not much to see here. Just the buttons and how to start the jobscheduler
 * references:
 * https://developer.android.com/reference/android/app/job/JobScheduler.html
 * https://developer.android.com/reference/android/app/job/JobInfo.Builder.html
 * https://github.com/googlesamples/android-JobScheduler
 * http://www.vogella.com/tutorials/AndroidTaskScheduling/article.html
 */


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
