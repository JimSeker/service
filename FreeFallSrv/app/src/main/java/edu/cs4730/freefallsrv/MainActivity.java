package edu.cs4730.freefallsrv;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 *  This is a simple demo of starting a service based on the freefall code from the sensor repo
 *  This code only starts a service.
 *
 *  With the new background restrictions in API 27+, this app doesn't work very well anymore.
 *  On the upside, services can't just eat the battery anymore.
 */

public class MainActivity extends AppCompatActivity {

    Boolean started = true;
    Button myButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, MySensorService.class));
        myButton = findViewById(R.id.button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (started) {
                    stopService(new Intent(getApplicationContext(), MySensorService.class));
                    myButton.setText("Start Service");
                    started = false;
                } else {
                    startService(new Intent(getApplicationContext(), MySensorService.class));
                    myButton.setText("Stop Service");
                    started = true;
                }
            }
        });

    }
    @Override
    public void onStop() {
        //if we don't stopService it will continue even after the activity has.
        //in this case, the phone will continue to scream even after the activity is gone.
        //uncomment line below, if you want to be sure, otherwise, for this example, I want to
        //be able to leave the service running.

        //stopService(new Intent(this,MySensorService.class));
        super.onStop();
    }
}
