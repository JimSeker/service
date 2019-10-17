package edu.cs4730.workmanagerdemo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * very simple worker to fake some work and allow the MainActivity to observe.  It is intended to run
 * for 2 minutes (maybe shorter) so the UI can update and say its working.  but really nothing
 * interesting happens in here.
 *
 * Please note, workerA will survive ending this program, so when testing be very careful or you
 * will have many many many of them running as I found out.  They will be restarted with the new code
 * though.  hence all the logging here.
 */
public class WorkerA extends Worker {
    public WorkerA(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        //sleep for 5 seconds, and run for 1 minutes, ie 12 times through the loop.
        for (int i =0; i<12; i++) {
            // Process work here...  we'll pretend by sleeping.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return Result.failure();  //we have failed, do retry
                //return Result.RETRY;  //we have failed, but retry later on.
            }
            Log.d("workerA", "i is " +i);
        }
        Log.d("workerA", "done.");
        return Result.success();
    }
}
