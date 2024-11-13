package edu.cs4730.jobintentservicedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.cs4730.jobintentservicedemo.databinding.ActivityMainBinding;

/**
 * All this example does is take in a number and start up the jobIntent service to complete the work.
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
                Intent i = new Intent(getApplicationContext(), MyJobIntentService.class);  //is any of that needed?  idk.
                //note, putExtra remembers type and I need this to be an integer.  so get an integer first.
                i.putExtra("times", Integer.parseInt(binding.editText.getText().toString()));  //should do error checking here!
                MyJobIntentService.enqueueWork(getApplicationContext(), i);
            }
        });
    }
}
