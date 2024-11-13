package edu.cs4730.jobservicejobworkitemdemo;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PersistableBundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Map;

import edu.cs4730.jobservicejobworkitemdemo.databinding.ActivityMainBinding;

/**
 * this demo shows how the workItem enqueue/dequeue works.  Note this API 26+
 * https://developer.android.com/reference/android/app/job/JobScheduler.html#enqueue(android.app.job.JobInfo,%20android.app.job.JobWorkItem)
 * https://developer.android.com/reference/android/app/job/JobParameters.html#dequeueWork()
 */


public class MainActivity extends AppCompatActivity {
    public static String id1 = "test_channel_01";
    ActivityResultLauncher<String[]> rpl;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.POST_NOTIFICATIONS};
    JobScheduler jobScheduler;
    JobInfo job;
    ActivityMainBinding binding;
    public static String TAG = "MainActivity";


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
        
        // for notifications permission now required in api 33
        //this allows us to check with multiple permissions, but in this case (currently) only need 1.
        rpl = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> isGranted) {
                boolean granted = true;
                for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                    logthis(x.getKey() + " is " + x.getValue());
                    if (!x.getValue()) granted = false;
                }
                if (granted) logthis("Permissions granted for api 33+");
            }
        });



        //get the Job Scheduler
        jobScheduler = getSystemService(JobScheduler.class);
        //get the job we will be sending workitems too.
        job = getJobInfo();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //let's create 3 fake work items and enqueue them.
                JobWorkItem item;
                Intent i;
                //first item
                i = new Intent();
                i.putExtra("Key1", "first item"); //fake data too.
                item = new JobWorkItem(i);
                jobScheduler.enqueue(job, item);
                //second item
                i = new Intent();
                i.putExtra("Key1", "Second item"); //fake data too.
                item = new JobWorkItem(i);
                jobScheduler.enqueue(job, item);
                //first item
                i = new Intent();
                i.putExtra("Key1", "Third item"); //fake data too.
                item = new JobWorkItem(i);
                jobScheduler.enqueue(job, item);

            }
        });


        createChannel();  //needed for the persistent notification created in service.
        //for the new api 33+ notifications permissions.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!allPermissionsGranted()) {
                rpl.launch(REQUIRED_PERMISSIONS);
            }
        }

    }


    //This is a helper method to schedule the job.  It doesn't need to be declared here, but it
    //a good way to keep everything together.

    // schedule the start of the service every 10 - 30 seconds
    public JobInfo getJobInfo() {

        ComponentName serviceComponent = new ComponentName(getApplicationContext(), MyJobWorkService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(10 * 1000); // wait at least
        builder.setOverrideDeadline(30 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        //builder.setRequiresBatteryNotLow(true);  //only when the batter is not low.  API 26+

        return builder.build();
    }

    /**
     * Create notificaiton channel required in  API 26+
     */
    private void createChannel() {

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(id1, getString(R.string.channel_name),  //name of the channel
                NotificationManager.IMPORTANCE_LOW);   //importance level
        //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
        // Configure the notification channel.
        mChannel.setDescription(getString(R.string.channel_description));
        mChannel.enableLights(true);
        // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
        mChannel.setShowBadge(true);
        nm.createNotificationChannel(mChannel);
    }
    //ask for permissions when we start.
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void logthis(String msg) {
        Log.d(TAG, msg);
    }
}
