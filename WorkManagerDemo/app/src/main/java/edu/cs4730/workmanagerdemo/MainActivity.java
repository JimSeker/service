package edu.cs4730.workmanagerdemo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

/**
 * need a simple worker, parameter worker.  Then chain them together for the last example.  maybe a parallel
 * <p>
 * As of Oct 9, 2018, this can't converted to androidx yet.
 * see https://developer.android.com/topic/libraries/architecture/adding-components
 * <p>
 * This is using alpha libraries, which means (like between 9 and 10), method change names or are removed.
 * So make sure to look at https://developer.android.com/jetpack/docs/release-notes  to see if the
 * WorkManager has been changed again.   things that say, "Breaking API Changes"
 *
 * As of Oct 24, 2018, It can be converted to androidx, because it looking for support.liveData and
 * not androidx.livedata.
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
     *  This will create a oneshot workerA task and schedule it to run once.
     *  commented out code shows how to make it recur every 24 hours.
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
        WorkManager.getInstance().enqueue(runWorkA);

        //not necessary, but this will tell us the status of the task.
        LiveData<WorkStatus> status = WorkManager.getInstance().getStatusByIdLiveData(runWorkA.getId());
        status.observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
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
        WorkManager.getInstance().enqueue(mathWork);

        //now set the observer to get the result.
        WorkManager.getInstance().getStatusByIdLiveData(mathWork.getId())
            .observe(this, new Observer<WorkStatus>() {
                @Override
                public void onChanged(@Nullable WorkStatus status) {
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
        WorkManager.getInstance()
            // First, run all the A tasks (in parallel):
            .beginWith(runWorkA, runWorkB)
            // ...when all A tasks are finished, run the single B task:
            .then(runWorkC)
            .enqueue();


        // not necessary, but so the display updates get the LiveData for each and set to update the textviews.

        LiveData<WorkStatus> status = WorkManager.getInstance().getStatusByIdLiveData(runWorkA.getId());
        status.observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
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

        LiveData<WorkStatus> statusb = WorkManager.getInstance().getStatusByIdLiveData(runWorkB.getId());
        statusb.observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
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

        LiveData<WorkStatus> statusc = WorkManager.getInstance().getStatusByIdLiveData(runWorkC.getId());
        statusc.observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
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
