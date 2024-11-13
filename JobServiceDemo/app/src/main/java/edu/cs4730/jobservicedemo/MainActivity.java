package edu.cs4730.jobservicedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;

import edu.cs4730.jobservicedemo.databinding.ActivityMainBinding;

/**
 * not much to see here. Just the buttons and how to start the jobscheduler
 * references:
 * https://developer.android.com/reference/android/app/job/JobScheduler.html
 * https://developer.android.com/reference/android/app/job/JobInfo.Builder.html
 * https://github.com/googlesamples/android-JobScheduler
 * http://www.vogella.com/tutorials/AndroidTaskScheduling/article.html
 */
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobService.scheduleJob(MainActivity.this, Integer.parseInt(binding.editText.getText().toString()), false);  //mainActivity context, not listener...
            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobService.scheduleJob(MainActivity.this, Integer.parseInt(binding.editText.getText().toString()), true);  //mainActivity context, not listener...
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobService.cancelJob(MainActivity.this);
            }
        });
    }
}
