package edu.cs4730.jobintentservicedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.core.app.JobIntentService;

import android.util.Log;
import android.widget.Toast;

import java.security.SecureRandom;

/**
 * This is a simple job to take a number and toast that number of random numbers, one every 10 seconds.
 */
public class MyJobIntentService extends JobIntentService {

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;
    final String TAG = "MyJobIntenetService";

    // Random number generator
    private final SecureRandom mGenerator = new SecureRandom();

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.

        int top = intent.getIntExtra("times", 5);  //default is 5.
        int num;

        for (int i = 0; i < top; i++) {
            num = mGenerator.nextInt(100);
            toast(i+ " Random number is " + num);
            Log.wtf(TAG, i + " Random number is " + num);
            try {
                Thread.sleep(10000);  // 1000 is one second, ten seconds would be 10000
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.wtf(TAG, "All work complete");
        toast("All work complete");
    }

    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}