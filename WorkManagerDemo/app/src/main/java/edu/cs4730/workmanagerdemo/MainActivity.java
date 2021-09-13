package edu.cs4730.workmanagerdemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

/**
 * need a simple worker, parameter worker.  Then chain them together for the last example.  maybe a parallel
 * Make sure you are looking at the logcat as well.  You can see what the workers are doing.
 * <p>
 * see https://developer.android.com/topic/libraries/architecture/adding-components
 * <p>
 *
 *  NOTE, currently can't be run at API 31.    Error:
 *   java.lang.IllegalArgumentException: edu.cs4730.workmanagerdemo: Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
 *     Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
 * except, I have no pendingIntents, so it got to be something in the workmanager libriry.
 */

public class MainActivity extends AppCompatActivity {

    TextView tv_oneshot, tv_param;
    TextView tv_chaina, tv_chainb, tv_chainc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textviews for the buttons to show what is going on.
        tv_oneshot = findViewById(R.id.tv_oneshot);
        tv_param = findViewById(R.id.tv_param);
        tv_chaina = findViewById(R.id.tv_chaina);
        tv_chainb = findViewById(R.id.tv_chainb);
        tv_chainc = findViewById(R.id.tv_chainc);

        //set the click listeners for the three buttons.
        findViewById(R.id.btn_oneshot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneshot();
            }
        });

        findViewById(R.id.btn_param).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                param();
            }
        });

        findViewById(R.id.btn_chain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chain();
            }
        });

    }


    /**
     * This will create a oneshot workerA task and schedule it to run once.
     * commented out code shows how to make it recur every 24 hours.
     */
    public void oneshot() {
        //for a schedule once
        OneTimeWorkRequest runWorkA = new OneTimeWorkRequest.Builder(WorkerA.class)
                .build();

        /*
        //for recur schedule, say every 24 hours, comment out above, since duplicate variables.
        PeriodicWorkRequest.Builder workerkBuilder =
            new PeriodicWorkRequest.Builder(WorkerA.class, 24,  TimeUnit.HOURS);
        // ...if you want, you can apply constraints to the builder here...
        Constraints myConstraints = new Constraints.Builder()
            //.setRequiresDeviceIdle(true)
            .setRequiresCharging(true)
            // Many other constraints are available, see the
            // Constraints.Builder reference
            .build();

       // Create the actual work object:
        PeriodicWorkRequest runWorkA = workerkBuilder.setConstraints(myConstraints)
        .build();
       */

        //now enqueue the task so it can be run.
        WorkManager.getInstance(getApplicationContext()).enqueue(runWorkA);

        //not necessary, but this will tell us the status of the task.
        LiveData<WorkInfo> status = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkA.getId());
        status.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        tv_oneshot.setText("Status is Blocked");
                        break;
                    case CANCELLED:
                        tv_oneshot.setText("Status is canceled");
                        break;
                    case ENQUEUED:
                        tv_oneshot.setText("Status is enqueued");
                        break;
                    case FAILED:
                        tv_oneshot.setText("Status is failed");
                        break;
                    case RUNNING:
                        tv_oneshot.setText("Status is running");
                        break;
                    case SUCCEEDED:
                        tv_oneshot.setText("Status is succeeded");
                        break;
                    default:
                        tv_oneshot.setText("Status is unknown");
                }

            }
        });
    }

    //This shows how to set input and receive a result for a worker task.
    public void param() {
        // Create the Data object:
        final Data myData = new Data.Builder()
                // We need to pass three integers: X, Y, and Z
                .putInt(WorkerParameters.KEY_X_ARG, 42)
                // ... and build the actual Data object:
                .build();

        // ...then create and enqueue a OneTimeWorkRequest that uses those arguments
        OneTimeWorkRequest mathWork = new OneTimeWorkRequest.Builder(WorkerParameters.class)
                .setInputData(myData)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(mathWork);

        //now set the observer to get the result.
        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(mathWork.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo status) {
                        if (status != null && status.getState().isFinished()) {
                            int myResult = status.getOutputData().getInt(WorkerParameters.KEY_RESULT,
                                    -1);
                            tv_param.setText("Result is " + myResult);
                        }
                    }
                });
    }

    //This method changes together WorkerA and B in parallel and then C.
    public void chain() {
        //first setup the requests
        OneTimeWorkRequest runWorkA = new OneTimeWorkRequest.Builder(WorkerA.class).build();
        OneTimeWorkRequest runWorkB = new OneTimeWorkRequest.Builder(WorkerB.class).build();
        OneTimeWorkRequest runWorkC = new OneTimeWorkRequest.Builder(WorkerC.class).build();
        //now setup them up to run, A and B together.  Once they are complete then launch C.
        WorkManager.getInstance(getApplicationContext())
                // First, run all the A tasks (in parallel):
                .beginWith(Arrays.asList(runWorkA, runWorkB))
                // ...when all A tasks are finished, run the single B task:
                .then(runWorkC)
                .enqueue();


        // not necessary, but so the display updates get the LiveData for each and set to update the textviews.

        LiveData<WorkInfo> status = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkA.getId());
        status.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        tv_chaina.setText("A Status is Blocked");
                        break;
                    case CANCELLED:
                        tv_chaina.setText("A Status is canceled");
                        break;
                    case ENQUEUED:
                        tv_chaina.setText("A Status is enqueued");
                        break;
                    case FAILED:
                        tv_chaina.setText("A Status is failed");
                        break;
                    case RUNNING:
                        tv_chaina.setText("A Status is running");
                        break;
                    case SUCCEEDED:
                        tv_chaina.setText("A Status is succeeded");
                        break;
                    default:
                        tv_chaina.setText("A Status is unknown");
                }
            }
        });

        LiveData<WorkInfo> statusb = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkB.getId());
        statusb.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        tv_chainb.setText("B Status is Blocked");
                        break;
                    case CANCELLED:
                        tv_chainb.setText("B Status is canceled");
                        break;
                    case ENQUEUED:
                        tv_chainb.setText("B Status is enqueued");
                        break;
                    case FAILED:
                        tv_chainb.setText("B Status is failed");
                        break;
                    case RUNNING:
                        tv_chainb.setText("B Status is running");
                        break;
                    case SUCCEEDED:
                        tv_chainb.setText("B Status is succeeded");
                        break;
                    default:
                        tv_chainb.setText("B Status is unknown");
                }
            }
        });

        LiveData<WorkInfo> statusc = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkC.getId());
        statusc.observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        tv_chainc.setText("C Status is Blocked");
                        break;
                    case CANCELLED:
                        tv_chainc.setText("C Status is canceled");
                        break;
                    case ENQUEUED:
                        tv_chainc.setText("C Status is enqueued");
                        break;
                    case FAILED:
                        tv_chainc.setText("C Status is failed");
                        break;
                    case RUNNING:
                        tv_chainc.setText("C Status is running");
                        break;
                    case SUCCEEDED:
                        tv_chainc.setText("C Status is succeeded");
                        break;
                    default:
                        tv_chainc.setText("C Status is unknown");
                }
            }
        });
    }
}
