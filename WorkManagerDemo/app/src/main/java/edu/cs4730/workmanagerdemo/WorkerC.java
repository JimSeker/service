package edu.cs4730.workmanagerdemo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * This code is the same as WorkerA (and B).  It is purely to demo
 * chaining multiple tasks together.
 */

public class WorkerC extends Worker {
    String TAG = "WorkerC";

    public WorkerC(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        //sleep for 5 seconds, and run for 1 minutes, ie 12 times through the loop.
        for (int i = 0; i < 12; i++) {
            // Process work here...  we'll pretend by sleeping.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return Result.failure();  //we have failed, do retry
                //return Result.RETRY;  //we have failed, but retry later on.
            }
            Log.d(TAG, "i is " + i);
        }
        Log.d(TAG, "done.");
        return Result.success();
    }

}
