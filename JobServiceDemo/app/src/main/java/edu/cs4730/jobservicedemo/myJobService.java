package edu.cs4730.jobservicedemo;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class myJobService extends JobService {

    private static final String TAG = "myJobService";
    // Random number generator
    private final Random mGenerator = new Random();

    public myJobService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        // The work that this service "does" is simply wait for a certain duration and finish
        // the job (on another thread).

        int max = params.getExtras().getInt("max", 6);  //something low so I know it didn't work.
        Log.wtf(TAG, "max is " + max);

        // Process work here...  we'll pretend by sleeping for 3 seconds.
 /*       try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
*/
        Toast.makeText(getApplicationContext(), "Job: number is " + mGenerator.nextInt(max), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Job: I'm working on something...");

        // Return true as there's more work to be done with this job.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Stop tracking these job parameters, as we've 'finished' executing.
        Log.i(TAG, "on stop job: " + params.getJobId());

        // Return false to drop the job.
        return false;
    }


    //This is a helper method to schedule the job.  It doesn't need to be declared here, but it
    //a good way to keep everything together.

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context, int max, boolean recurring) {

        ComponentName serviceComponent = new ComponentName(context, myJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        if (recurring) {
            builder.setPeriodic(15* 10000); //only once every 15 seconds.
            //builder.setPersisted(true);  //will persist across reboots.
            //except this runs in about 10 to 30 minute intervals...  Likely a min threshold here.
            Log.wtf(TAG, "set recurring");
        } else {  //just set it for once, between 10 to 30 seconds from now.
            builder.setMinimumLatency(10 * 1000); // wait at least
            builder.setOverrideDeadline(30 * 1000); // maximum delay
            Log.wtf(TAG, "set once");
        }

        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        //builder.setRequiresBatteryNotLow(true);  //only when the batter is not low.  API 26+
        //set some data via a persistablebundle.
        PersistableBundle extras = new PersistableBundle();
        extras.putInt("max", max);
        builder.setExtras(extras);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    // cancel all the jobs.
    public static void cancelJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JobScheduler.class);
        jobScheduler.cancelAll();
    }

}
