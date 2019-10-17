package edu.cs4730.workmanagerdemo;

import android.content.Context;
import androidx.annotation.NonNull;

import java.util.Random;

import androidx.work.Data;
import androidx.work.Worker;

/**
 * this takes a parameter and returns a value
 * the input x is top number for the random number generator
 * and result is the random number.
 *
 */

public class WorkerParameters extends Worker {
    // Define the parameter keys:
    public static final String KEY_X_ARG = "X";
    // ...and the result key:
    public static final String KEY_RESULT = "result";

    Random r;

    public WorkerParameters(@NonNull Context context, @NonNull androidx.work.WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        //we are pretending this is some complex work to be done.   instead of a random number.
        r = new Random();
        // Fetch the arguments (and specify default values):
        int x = getInputData().getInt(KEY_X_ARG, 0);

        int result = r.nextInt(x);


        //...set the output, and we're done!
        Data output = new Data.Builder()
            .putInt(KEY_RESULT, result)
            .build();

        return Result.success(output);
    }
}
