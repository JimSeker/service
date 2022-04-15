package edu.cs4730.servicedemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * A simple example of how to call/start services.
 */

public class MainActivity extends AppCompatActivity {

    public static String id = "test_channel_01";

    TextView logger;
    String TAG = "MainActivity";

    /**
     * The service will call the handler to send back information.
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Object path = msg.obj;
            Toast.makeText(getApplicationContext(), path.toString(), Toast.LENGTH_LONG).show();
            logger.append(path.toString() + "\n");
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //logger for the permission checking for the file download service.
        logger = findViewById(R.id.logger);

        //IntentService start with 5 random number toasts
        findViewById(R.id.btn_istarth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent number5 = new Intent(getApplicationContext(), myIntentService.class);
                number5.putExtra("times", 5);
                Messenger messenger = new Messenger(handler);
                number5.putExtra("MESSENGER", messenger);
                startService(number5);
            }
        });

        //IntentService starts and creates 5 notifications with random numbers.
        findViewById(R.id.btn_istartn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent number5 = new Intent(getApplicationContext(), myIntentService.class);
                number5.putExtra("times", 5);
                startService(number5);
            }
        });
        //Service start with 5 random number toasts
        findViewById(R.id.btn_sstarth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent number5 = new Intent(getApplicationContext(), myService.class);
                number5.putExtra("times", 5);
                Messenger messenger = new Messenger(handler);
                number5.putExtra("MESSENGER", messenger);
                startService(number5);
            }
        });

        //Service start with 5 random number and sends notifications.
        findViewById(R.id.btn_sstartn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent number5 = new Intent(getApplicationContext(), myService.class);
                number5.putExtra("times", 5);
                startService(number5);
            }
        });

        createchannel();  //creates the channels for notifications if needed.
    }

    /**
     * for API 26+ create notification channels
     */
    private void createchannel() {

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(id,
            getString(R.string.channel_name),  //name of the channel
            NotificationManager.IMPORTANCE_DEFAULT);   //importance level
        //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
        // Configure the notification channel.
        mChannel.setDescription(getString(R.string.channel_description));
        mChannel.enableLights(true);
        //Sets the notification light color for notifications posted to this channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setShowBadge(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        nm.createNotificationChannel(mChannel);

    }
}
